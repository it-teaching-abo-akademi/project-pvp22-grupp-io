package pvp.api.product;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.json.JSONObject;
import pvp.models.interfaces.Product;

import java.io.IOException;

public class ProductDeserializer extends StdDeserializer<Product> {
    public ProductDeserializer() {
        this(null);
    }

    protected ProductDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Product deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.readValueAsTree();
        Product product = pvp.api.product.Product.getObjectFromJson(new JSONObject(node.toString()));
        return product;
    }
}
