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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class OrderDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<Order> selectAllOrders() {
        String sql =
                "SELECT * " +
                "FROM \"order\"";

        return jdbcTemplate.query(sql, mapOrdersFomDb());
    }

    List<Order> selectIncompleteOrders() {
        String sql =
                "SELECT * " +
                "FROM \"order\"" +
                "WHERE completed = false";
        return jdbcTemplate.query(sql, mapOrdersFomDb());
    }

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

    List<OrderLine> getOrderLinesByOrderId(int orderId) {
        String sql = "" +
                "SELECT *" +
                "FROM order_line " +
                "WHERE order_id = '" + orderId + "'";
        return jdbcTemplate.query(sql, mapOrderLinesFromDb());
    }

    int insertOrder(Order order) {
        Order dbOrder = this.getOrderById(order.getPk());

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        if (dbOrder == null) {
            String sql = "" +
                    "INSERT INTO \"order\" (" +
                    " user_id," +
                    " total_price," +
                    " completed) "+
                    "VALUES (?, ?, ?)";

            int returnValue = jdbcTemplate.update(conn -> {
                        PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                        // Set parameters
                        preparedStatement.setInt(1, order.getUserId());
                        preparedStatement.setInt(2, order.getTotalPrice());
                        preparedStatement.setBoolean(3, order.isComplete());

                        return preparedStatement;

                    }, generatedKeyHolder
            );
        } else {
            String sql = "" +
                    "UPDATE \"order\"" +
                    " SET user_id = " + order.getUserId() + "," +
                    " total_price = " + order.getTotalPrice() + "," +
                    " completed = " + order.isComplete() + " " +
                    "WHERE id = " + order.getPk();

            int returnValue = jdbcTemplate.update(conn -> {
                        PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                        return preparedStatement;
                    }, generatedKeyHolder
            );
        }
        List<Map<String, Object>> keys = generatedKeyHolder.getKeyList();
        int orderId = (int) keys.get(0).get("id");

        jdbcTemplate.update("DELETE FROM order_line WHERE order_id = " + orderId + ";");

        String orderLineSql = "INSERT INTO order_line (" +
                " total_price, " +
                " quantity, " +
                " order_id, " +
                " product_id) "+
                "VALUES (?, ?, ?, ?)";
        Set<OrderLine> orderLines = order.getOrderLines();

        orderLines.stream().forEach(orderLine -> {
            jdbcTemplate.update(
                    orderLineSql,
                    orderLine.getTotalPrice(),
                    orderLine.getQuantity(),
                    orderId,
                    2
            );
        });

        return orderId;
    }

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
