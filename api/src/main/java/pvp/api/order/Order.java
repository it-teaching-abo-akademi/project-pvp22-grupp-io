package pvp.api.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pvp.models.interfaces.User;

import pvp.models.interfaces.Payment;
import pvp.models.interfaces.OrderLine;
import java.util.Set;


@JsonDeserialize(using=OrderDeserializer.class)
public class Order extends pvp.models.Order {

    /**
     * Order()
     * Extends the base Order with JsonProperties to enable the serialization/deserialization functionality.
     * @param pk - pk of order
     * @param order_total - total of order.
     * @param orderLines - orderlines of order.
     * @param user - user of order.
     * @param payments - payments in order.
     * @param complete - state of completion: uncomplete (false) or complete (true).
     */
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