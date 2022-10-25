package pvp.api.payments;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import pvp.models.interfaces.Payment;

import java.io.IOException;

public class PaymentDeserializer extends StdDeserializer<Payment> {

    public PaymentDeserializer() {
        this(null);
    }
    protected PaymentDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Payment deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.readValueAsTree();
        JsonNode pkNode = node.get("pk");

        Integer pk = null;
        if (pkNode != null) {
            pk = pkNode.asInt();
        }

        Payment payment = new pvp.api.payments.Payment(
                pk,
                node.get("amount").asInt(),
                node.get("order_id").asInt(),
                node.get("payment_type_id").asText()
        );

        return payment;
    }
}
