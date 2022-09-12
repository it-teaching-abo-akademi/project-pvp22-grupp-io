package pvp.models;

import pvp.models.abstractModels.PkModel;

import java.util.Set;

public class Order extends PkModel {
    private int totalPrice;
    private Set<OrderLine> orderLines;
    private User user;

    public Order(String pk, int totalPrice, Set<OrderLine> orderLines, User user) {
        super(pk);
        this.totalPrice = totalPrice;
        this.orderLines = orderLines;
        this.user = user;
    }

    public int getTotalPrice(){
        return this.totalPrice;
    }

    public Set<OrderLine> getOrderLines() {
        return this.orderLines;
    }

    public User getUser() {
        return this.user;
    }
}