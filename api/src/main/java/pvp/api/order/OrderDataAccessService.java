package pvp.api.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import pvp.api.payments.PaymentLineDataAccessService;
import pvp.api.product.ProductDataAccessService;
import pvp.api.user.UserDataAccessService;
import pvp.models.interfaces.OrderLine;
import pvp.models.interfaces.Payment;
import pvp.models.interfaces.Product;
import pvp.models.interfaces.User;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class OrderDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * selectAllOrders()
     *
     * Selects all orders from the postgreSQL database and returns a list with all the orders.
     */
    List<Order> selectAllOrders() {
        String sql =
                "SELECT * " +
                "FROM \"order\"";

        return jdbcTemplate.query(sql, mapOrdersFomDb());
    }

    /**
     * selectIncompleteOrders()
     *
     * Selects all incomplete orders from the postgreSQL database and returns a list with all the incomplete orders.
     */
    List<Order> selectIncompleteOrders() {
        String sql =
                "SELECT * " +
                "FROM \"order\"" +
                "WHERE completed = false";
        return jdbcTemplate.query(sql, mapOrdersFomDb());
    }

    /**
     * getOrderById()
     *
     * Fetches an order from the postgreSQL database based on its ID.
     * @param id - the id of the order being searched for.
     */
    Order getOrderById(int id) {
        String sql = "" +
                "SELECT *" +
                "FROM \"order\" " +
                "WHERE id = '" + id + "'";
        List<Order> orders = jdbcTemplate.query(sql, mapOrdersFomDb());
        if (orders.size() > 0) {
            return orders.get(0);
        }
        return null;
    }

    /**
     * getOrderLinesByOrderId()
     *
     * Fetches a list of an order's orderlines from the postgreSQL database based on the order's ID.
     * @param orderId - the id of the order.
     */
    List<OrderLine> getOrderLinesByOrderId(int orderId) {
        String sql = "" +
                "SELECT *" +
                "FROM order_line " +
                "WHERE order_id = '" + orderId + "'";
        return jdbcTemplate.query(sql, mapOrderLinesFromDb());
    }

    /**
     * insertOrder()
     *
     * Checks if order exists in database, if it does
     */
    int insertOrder(Order order) {
        Order dbOrder;
        try {
            dbOrder = this.getOrderById(order.getPk());
        } catch (NullPointerException e) {
            dbOrder = null;
        }
        LocalDate nowDate = LocalDate.now();
        String now = nowDate.toString().replace("-", "");
        Integer userId = order.getUserId();
        if (userId == 0) {
            userId = null;
        }

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        if (dbOrder == null) {  // If an order does not exist, a new one is created.
                String sql = "" +
                        "INSERT INTO \"order\" (" +
                        " total_price," +
                        " completed," +
                        " purchase_date";
                if (userId != null) {
                    sql = sql + ", user_id";
                }
                sql = sql +
                        ") "+
                        "VALUES (?, ?, '" + now + "'";
                if (userId != null) {
                    sql = sql + ", ?";
                }
                sql = sql + ")";
                String finalSql = sql;
                Integer finalUserId = userId;
                int returnValue = jdbcTemplate.update(conn -> {
                            PreparedStatement preparedStatement = conn.prepareStatement(finalSql, Statement.RETURN_GENERATED_KEYS);

                            // Set parameters
                            preparedStatement.setInt(1, order.getTotalPrice());
                            preparedStatement.setBoolean(2, order.isComplete());
                            if (finalUserId != null) {
                                preparedStatement.setInt(4, finalUserId);
                            }
                            return preparedStatement;

                        }, generatedKeyHolder
                );
        } else {  // If an order does exist, it is updated.
            String sql = "" +
                    "UPDATE \"order\"" +
                    " SET" +
                    " total_price = '" + order.getTotalPrice() + "'," +
                    " completed = '" + order.isComplete() + "'," +
                    " purchase_date = '" + now + "' ";


            if (userId != null) {
                sql = sql + ", user_id = '" + order.getUserId() + "',";
            }
            sql = sql + "WHERE id = " + order.getPk();

            String finalSql = sql;
            int returnValue = jdbcTemplate.update(conn -> {
                        PreparedStatement preparedStatement = conn.prepareStatement(finalSql, Statement.RETURN_GENERATED_KEYS);
                        return preparedStatement;
                    }, generatedKeyHolder
            );
        }
        List<Map<String, Object>> keys = generatedKeyHolder.getKeyList();
        int orderId = (int) keys.get(0).get("id");

        jdbcTemplate.update("DELETE FROM order_line WHERE order_id = " + orderId + ";");

        String orderLineSql = "INSERT INTO order_line (" + //query to enter values into the orderline.
                " total_price, " +
                " quantity, " +
                " order_id, " +
                " product_id) "+
                "VALUES (?, ?, ?, ?)";
        Set<OrderLine> orderLines = order.getOrderLines();

        orderLines.stream().forEach(orderLine -> { // updates/enters values into the orderline.
            jdbcTemplate.update(
                    orderLineSql,
                    orderLine.getTotalPrice(),
                    orderLine.getQuantity(),
                    orderId,
                    orderLine.getProductId()
            );
        });

        return orderId;
    }

    /**
     * mapOrderLinesFromDb()
     *
     * Returns a arrow-function that can be run to generate orderline from a database row.
     */
    private RowMapper<OrderLine> mapOrderLinesFromDb() {
        ProductDataAccessService productService = new ProductDataAccessService(this.jdbcTemplate);
        return (resultSet, i) -> {
            Integer pk = resultSet.getInt("id");
            Integer productId = resultSet.getInt("product_id");
            int unitPrice = resultSet.getInt("unit_price");
            int quantity = resultSet.getInt("quantity");
            int totalPrice = resultSet.getInt("total_price");
            Product product = null;
            if (productId != null) {
                product = productService.getProductById(productId);
            }

            return new pvp.api.order.OrderLine(
                    pk,
                    product,
                    unitPrice,
                    quantity,
                    totalPrice
            );
        };
    }

    /**
     * mapOrderLinesFromDb()
     *
     * Returns a arrow-function that can be run to generate order from a database row.
     */
    private RowMapper<Order> mapOrdersFomDb() {
        UserDataAccessService userService = new UserDataAccessService(this.jdbcTemplate);
        PaymentLineDataAccessService paymentService = new PaymentLineDataAccessService(this.jdbcTemplate);
        return (resultSet, i) -> {
            int pk = resultSet.getInt("id");
            Integer user_id = resultSet.getInt("user_id");
            User user = null;
            if (user_id != null) {
                user = userService.getUserById(resultSet.getInt("user_id"));
            }
            Set<pvp.models.interfaces.OrderLine> orderLines = getOrderLinesByOrderId(pk).stream().collect(Collectors.toSet());
            Set<Payment> payments = paymentService.getPaymentsByOrderId(pk).stream().collect(Collectors.toSet());
            int total_price = resultSet.getInt("total_price");
            boolean complete = resultSet.getBoolean("completed");

            return new Order(
                    pk,
                    total_price,
                    orderLines,
                    user,
                    payments,
                    complete
            );
        };
    }
}
