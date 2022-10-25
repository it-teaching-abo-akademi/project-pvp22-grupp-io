package pvp.api.product;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Product extends pvp.models.Product implements Serializable {
    public Product(
            @JsonProperty("pk") Integer pk,
            @JsonProperty("sku") String sku,
            @JsonProperty("soldCount") int soldCount,
            @JsonProperty("price") int price,
            @JsonProperty("name") String name
    ) {
        super(pk, price, name, sku, soldCount);
    }
}
