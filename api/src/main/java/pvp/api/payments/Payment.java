package pvp.api.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pvp.api.order.OrderDeserializer;
import pvp.models.interfaces.Order;

import java.io.Serializable;

@JsonDeserialize(using= PaymentDeserializer.class)
public class Payment extends pvp.models.Payment implements Serializable {

    /**
     * Payment()
     * Extends the base Payment with JsonProperties to enable the serialization/deserialization functionality.
     * @param pk - the pk of the payment.
     * @param amount - the amount of the payment.
     * @param orderId - the order ID of the payment.
     * @param paymentType - the payment type of the payment.
     */
    public Payment(
            @JsonProperty("pk") Integer pk,
            @JsonProperty("amount") int amount,
            @JsonProperty("order_id") int orderId,
            @JsonProperty("payment_type_id") String paymentType
    ) {
        super(pk, amount, paymentType, orderId);
    }

    /**
     * Payment()
     * Extends the base Payment with JsonProperties to enable the serialization/deserialization functionality.
     * @param pk - the pk of the payment.
     * @param amount - the amount of the payment.
     * @param order - order of the payment.
     * @param paymentType - the payment type of the payment.
     */
    public Payment(
            @JsonProperty("pk") Integer pk,
            @JsonProperty("amount") int amount,
            @JsonProperty("order") Order order,
            @JsonProperty("payment_type_id") String paymentType
    ) {
        super(pk, amount, paymentType, order);
    }
}
