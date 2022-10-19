package pvp.cashier.models;

import javafx.beans.Observable;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javafx.collections.ObservableSet;
import pvp.models.User;
import pvp.models.interfaces.OrderLine;
import pvp.models.interfaces.Payment;
import pvp.models.interfaces.Product;

public class Order extends pvp.models.Order implements Serializable {

    public Order() {
        super(null,
                0,
                new HashSet<OrderLine>(),
                new User(1, UUID.randomUUID(), "Test user"),
                new HashSet<Payment>(),
                false);
    }

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
