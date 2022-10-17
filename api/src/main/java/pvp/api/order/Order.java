package pvp.api.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import pvp.models.interfaces.User;

import pvp.models.interfaces.Payment;
import pvp.models.interfaces.OrderLine;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@JsonDeserialize(using=OrderDeserializer.class)
public class Order extends pvp.models.Order {
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