package pvp.api.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@JsonDeserialize(using=ProductDeserializer.class)
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
            @JsonProperty("soldCount") Integer soldCount,
            @JsonProperty("price") int price,
            @JsonProperty("name") String name,
            @JsonProperty("vat") Integer vat,
            @JsonProperty("keywords") String keywords
    ) {
        super(pk, price, name, sku, soldCount, vat, keywords);
    }

    public static Product getObjectFromJson(JSONObject json) {
        Product product = new Product(
                json.getInt("pk"),
                json.getString("sku"),
                json.getInt("soldCount"),
                json.getInt("price"),
                json.getString("name"),
                json.getInt("vat"),
                ""
        );
        try {
            String keywords = json.getString("keywords");
            product.setKeywords(keywords);
        } catch (JSONException e) {
            JSONArray keywords = json.getJSONArray("keywords");
            Set<String> keywordSet = new HashSet<String>();
            keywords.forEach(keyword -> keywordSet.add((String) keyword));
            product.setKeywords(keywordSet);
        }
        return product;
    }
}
