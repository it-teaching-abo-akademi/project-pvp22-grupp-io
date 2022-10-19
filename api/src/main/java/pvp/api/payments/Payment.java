package pvp.api.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import pvp.models.interfaces.Order;

import java.io.Serializable;

public class Payment extends pvp.models.Payment implements Serializable {
    public Payment(
            @JsonProperty("pk") Integer pk,
            @JsonProperty("amount") int amount,
            @JsonProperty("order_id") int orderId,
            @JsonProperty("payment_type_id") String paymentType
    ) {
        super(pk, amount, paymentType, orderId);
    }

    public Payment(
            @JsonProperty("pk") Integer pk,
            @JsonProperty("amount") int amount,
            @JsonProperty("order") Order order,
            @JsonProperty("payment_type_id") String paymentType
    ) {
        super(pk, amount, paymentType, order);
    }
}
