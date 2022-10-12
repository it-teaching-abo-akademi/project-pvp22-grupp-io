package pvp.api.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import pvp.models.interfaces.User;
import pvp.models.interfaces.OrderLine;
import pvp.models.interfaces.Payment;

import java.io.Serializable;
import java.util.Set;

public class Order extends pvp.models.Order implements Serializable {
    public Order(
            @JsonProperty("pk") Integer pk,
            @JsonProperty("order_total") int order_total,
            @JsonProperty("order_lines") Set<OrderLine> orderLines,
            @JsonProperty("user") User user,
            @JsonProperty("payment_lines") Set<Payment> payments,
            @JsonProperty("complete") boolean complete
            ) {
        super(pk, order_total, orderLines, user, payments, complete);
    }
}
