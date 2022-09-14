package pvp.api.payments;

import com.fasterxml.jackson.annotation.JsonProperty;

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
}
