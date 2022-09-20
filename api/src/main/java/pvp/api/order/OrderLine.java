package pvp.api.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import pvp.models.User;
import pvp.models.interfaces.Product;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class OrderLine extends pvp.models.OrderLine implements Serializable {
    public OrderLine(
            @JsonProperty("pk") Integer pk,
            @JsonProperty("product_id") Integer productId,
            @JsonProperty("unitPrice") int unitPrice,
            @JsonProperty("quantity") int quantity,
            @JsonProperty("totalPrice") int totalPrice
    ) {
        super(pk, unitPrice, quantity, totalPrice, productId);
    }
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
