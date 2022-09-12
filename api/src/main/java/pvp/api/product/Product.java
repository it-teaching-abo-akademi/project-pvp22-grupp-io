package pvp.api.product;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Product extends pvp.models.Product {
    public Product(
            @JsonProperty("pk") String pk,
            @JsonProperty("sku") String sku,
            @JsonProperty("price") int price,
            @JsonProperty("name") String name
    ) {
        super(pk, sku, price, name);
    }
}
