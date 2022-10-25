package pvp.api.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import pvp.models.interfaces.Payment;
import pvp.models.PaymentType;

import java.util.Arrays;
import java.util.List;

@Repository
public class PaymentLineDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PaymentLineDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * selectAllPaymentLines()
     * selects all paymentlines  from the postgreSQL database.
     */
    List<Payment> selectAllPaymentLines() { //selects all paymentlines from the postgreSQL database.
        String sql = "" +
                "SELECT * " +
                "FROM payment";

        return jdbcTemplate.query(sql, mapPaymentsFomDb());
    }

    /**
     * getPaymentLineById()
     * selects all paymentLine by ID from the postgreSQL database.
     */
    Payment getPaymentLineById(int id) { //selects all paymentlines by ID from the postgreSQL database.
        String sql = "" +
                "SELECT *" +
                "FROM payment " +
                "WHERE id = '" + id + "'";
        List<Payment> payments = jdbcTemplate.query(sql, mapPaymentsFomDb());
        if (payments.size() > 0) {
            return payments.get(0);
        }
        return null;
    }

    /**
     * getPaymentsByOrderId()
     * selects all payment by Order ID from the postgreSQL database.
     */
    public List<Payment> getPaymentsByOrderId(int orderId) {
        String sql = "" +
                "SELECT *" +
                "FROM payment " +
                "WHERE order_id = '" + orderId + "'";
        return jdbcTemplate.query(sql, mapPaymentsFomDb());
    }

    /**
     * ensurePaymentTypes()
     * ensures that the payment types exist in the database.
     */
    private void ensurePaymentTypes(PaymentType paymentType) {
        String createPaymentTypesSql = "" +
            "INSERT INTO payment_type (" +
            " name," +
            " id) " +
            "VALUES (?, ?)";
        try {
            jdbcTemplate.update(
                    createPaymentTypesSql,
                    paymentType.name(),
                    paymentType.ordinal()
            );
        } catch (Exception e) {
            String updatePaymentTypeSql = "" +
                "UPDATE payment_type " +
                "SET name = '" + paymentType.name() + "' " +
                "WHERE id = " + paymentType.ordinal();
            jdbcTemplate.update(updatePaymentTypeSql);
        }
    }

    /**
     * insertPaymentLine()
     * Inserts paymentlines. If it does not succeed in finding payment type, the second insertPaymentLine is called and a payment type is created.
     */
    int insertPaymentLine(Payment payment) {
        try {
            Payment dbPayment = this.getPaymentLineById(payment.getPk());
            if (dbPayment == null ) {
                String sql = "" +
                        "INSERT INTO payment (" +
                        " amount," +
                        " payment_type_id, " +
                        " order_id) " +
                        "VALUES (?, ?, ?)";
                return jdbcTemplate.update(
                        sql,
                        payment.getAmount(),
                        payment.getPaymentTypeAsId(),
                        payment.getOrderId()
                );
            } else {
                String sql = "" +
                    "UPDATE payment " +
                    "SET amount = '" + payment.getAmount() + "', " +
                    "payment_type_id = '" + payment.getPaymentTypeAsId() + "', " +
                    "order_id = '" + payment.getOrderId() + "' " +
                    "WHERE id = " + payment.getPk();
                return jdbcTemplate.update(sql);
            }
        } catch (Exception e) {
            Arrays.stream(pvp.models.PaymentType.values()).forEach(
                paymentType -> {
                    ensurePaymentTypes(paymentType);
                }
            );
        }
        return this.insertPaymentLine(payment, true);
    }

    /**
     * insertPaymentLine()
     * inserts paymentline.
     */
    int insertPaymentLine(Payment payment, boolean looped) {
        String sql = "" +
                "INSERT INTO payment (" +
                " amount," +
                " payment_type_id, " +
                " order_id) " +
                "VALUES (?, ?, ?)";
        return jdbcTemplate.update(
                sql,
                payment.getAmount(),
                payment.getPaymentTypeAsId(),
                payment.getOrderId()
        );
    }

    /**
     * mapPaymentsFomDb()*
     * Returns a arrow-function that can be run to generate a payment from a database row.
     */
    private RowMapper<Payment> mapPaymentsFomDb() {
        return (resultSet, i) -> {
            Integer pk = resultSet.getInt("id");
            int amount = resultSet.getInt("amount");
            int paymentTypeId = resultSet.getInt("payment_type_id");
            String paymentTypeName = PaymentType.values()[paymentTypeId].name();
            int order_id = resultSet.getInt("order_id");

            return new pvp.api.payments.Payment(
                    pk,
                    amount,
                    order_id,
                    paymentTypeName
            );
        };
    }
}
