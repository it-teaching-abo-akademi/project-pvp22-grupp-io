package pvp.api.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import pvp.models.PaymentType;
import pvp.models.interfaces.Payment;

import java.util.Arrays;
import java.util.List;

@Repository
public class PaymentLineDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PaymentLineDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<Payment> selectAllPaymentLines() {
        String sql = "" +
                "SELECT * " +
                "FROM payment";

        return jdbcTemplate.query(sql, mapPaymentsFomDb());
    }

    Payment getPaymentLineById(String id) {
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

    public List<Payment> getPaymentsByOrderId(int orderId) {
        String sql = "" +
                "SELECT *" +
                "FROM payment " +
                "WHERE order_id = '" + orderId + "'";
        return jdbcTemplate.query(sql, mapPaymentsFomDb());
    }

    int insertPaymentLine(Payment payment) {
        try {
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
        } catch (Exception e) {
            System.out.println(e);
            String createPaymentTypesSql = "" +
                    "INSERT INTO payment_type (" +
                    " name," +
                    " id) " +
                    "VALUES (?, ?)";
            Arrays.stream(pvp.models.PaymentType.values()).forEach(
                    paymentType -> {
                        jdbcTemplate.update(
                                createPaymentTypesSql,
                                paymentType.name(),
                                paymentType.ordinal()
                        );
                    }
            );
        }
        return this.insertPaymentLine(payment, true);
    }

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
