package pvp.api.product;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Product extends pvp.models.Product implements Serializable {

    /**
     * Product()
     * Extends the base Product with JsonProperties to enable the serialization/deserialization functionality.
     * @param pk - pk of the product.
     * @param sku - sku of the product.
     * @param soldCount - soldCount of the product.
     * @param price - price of the product.
     * @param name - name of the product.
     */
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
