package pvp.api.order;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.w3c.dom.Node;
import pvp.api.user.User;
import pvp.models.interfaces.Order;
import pvp.models.interfaces.OrderLine;
import pvp.models.interfaces.Payment;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class OrderDeserializer extends StdDeserializer<Order> {

    public OrderDeserializer() {
        this(null);
    }
    protected OrderDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Order deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.readValueAsTree();
        ArrayNode paymentNode = (ArrayNode) node.get("payment_lines");
        ArrayNode orderLines = (ArrayNode) node.get("order_lines");
        Set<OrderLine> orderLineSet = new HashSet<OrderLine>();
        orderLines.elements().forEachRemaining(orderLine ->{
            JsonNode pkNode = orderLine.get("pk");
            Integer pk = null;
            if (pkNode != null) {
                pk = pkNode.asInt();
            }
            orderLineSet.add(new pvp.api.order.OrderLine(
                    pk,
                    orderLine.get("productId").asInt(),
                    orderLine.get("unitPrice").asInt(),
                    orderLine.get("quantity").asInt(),
                    orderLine.get("totalPrice").asInt()
            ));
        });
        Set<Payment> paymentNodeSet = new HashSet<Payment>();
        paymentNode.elements().forEachRemaining(payments ->{
            JsonNode pkNode = payments.get("pk");
            Integer pk = null;
            if (pkNode != null) {
                pk = pkNode.asInt();
            }
            paymentNodeSet.add(new pvp.api.payments.Payment(
                    pk,
                    payments.get("amount").asInt(),
                    payments.get("order_id").asInt(),
                    payments.get("payment_type_id").asText()
            ));
        });

        JsonNode user = node.get("user");
        System.out.println(node.toString());
        System.out.println(user.get("customerReference").toString());
        Order order = new pvp.api.order.Order(
                node.get("pk").asInt(),
                node.get("order_total").asInt(),
                orderLineSet,
                new User(
                    user.get("pk").asInt(),
                    UUID.fromString(user.get("customerReference").asText()),
                    user.get("name").asText()
                ),
                paymentNodeSet, node.get("complete").asBoolean());
        return order;
    }
}
