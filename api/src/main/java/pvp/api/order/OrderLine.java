package pvp.api.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import pvp.models.interfaces.Product;

import java.io.Serializable;

public class OrderLine extends pvp.models.OrderLine implements Serializable {

    /**
     * OrderLine()
     *
     * Extends the base OrderLines with JsonProperties to enable the serialization/deserialization functionality.
     * @param  - the pk of the orderline.
     * @param productId - the product ID of the orderline.
     * @param unitPrice - the unit price of the orderline.
     * @param quantity - the quantity of the orderline.
     * @param totalPrice - the total price of the orderline.
     */
    public OrderLine(
            @JsonProperty("pk") Integer pk,
            @JsonProperty("product_id") Integer productId,
            @JsonProperty("unitPrice") int unitPrice,
            @JsonProperty("quantity") int quantity,
            @JsonProperty("totalPrice") int totalPrice
    ) {
        super(pk, unitPrice, quantity, totalPrice, productId);
    }

    /**
     * OrderLine()
     *
     * Extend the base OrderLines with JsonProperties to enable the serialization/deserialization functionality.
     * @param pk - the pk of the orderline.
     * @param product - the product of the orderline.
     * @param unitPrice - the unit price of the orderline.
     * @param quantity - the quantity of the orderline.
     * @param totalPrice - the total price of the orderline.
     */
    public OrderLine(
            @JsonProperty("pk") Integer pk,
            @JsonProperty("product") Product product,
            @JsonProperty("unitPrice") int unitPrice,
            @JsonProperty("quantity") int quantity,
            @JsonProperty("totalPrice") int totalPrice
    ) {
        super(pk, unitPrice, quantity, totalPrice, product);
    }
}
