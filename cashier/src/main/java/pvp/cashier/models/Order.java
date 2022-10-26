package pvp.cashier.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import pvp.models.User;
import pvp.models.interfaces.OrderLine;
import pvp.models.interfaces.Payment;

public class Order extends pvp.models.Order implements Serializable {

    /**
     * Order()
     * Order properties are added the HashSet-value on OrderLine and Payment to enable Serializable.
     */
    public Order() {
        super(null,
                0,
                new HashSet<OrderLine>(),
                new User(1, UUID.randomUUID(), "Test user"),
                new HashSet<Payment>(),
                false);
    }

    /**
     * Order()
     * Order properties are fetched from the User interface.
     * @param pk - pk of order.
     * @param price - price of order.
     * @param orderLines - orderlines in order.
     * @param user - user of order.
     * @param payments - payments of order.
     * @param complete - complete or uncomplete status of order.
     */
    public Order(Integer pk,
                 Integer price,
                 Set<OrderLine> orderLines,
                 pvp.models.interfaces.User user,
                 Set<Payment> payments,
                 boolean complete) {
        super(pk,
                price,
                orderLines,
                user,
                payments,
                complete
        );
    }

}
