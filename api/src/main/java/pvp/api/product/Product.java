package pvp.api.product;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.UUID;

public class Product extends pvp.models.Product implements Serializable {
    public Product(
            @JsonProperty("pk") Integer pk,
            @JsonProperty("sku") String sku,
            @JsonProperty("price") int price,
            @JsonProperty("name") String name,
            @JsonProperty("sold_count") Integer sold_count
    ) {
        super(pk, price, name, sku, sold_count);
    }
}
