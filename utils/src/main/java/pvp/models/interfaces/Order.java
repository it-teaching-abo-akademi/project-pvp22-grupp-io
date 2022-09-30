package pvp.models.interfaces;

import pvp.models.PaymentType;

import java.util.Optional;
import java.util.Set;

public interface Order extends PkModel {
    // Base interface for the orders
    // Comment 2

    public void updateTotalPrice();
    public int getTotalPrice();

    public Set<OrderLine> getOrderLines();
    public void addOrderLine(OrderLine orderLine);
    public void addProduct(Product product);
    public Optional<OrderLine> getOrderLineById(int id);
    public void removeOrderLine(OrderLine orderLine);
    public void removeOrderLine(int orderLineId);

    public void setUser(User user);
    public void setUserId(int id);
    public User getUser();
    public int getUserId();

    public Payment createPayment(int amount, PaymentType paymentType, Order order);
    public Payment createPayment(int amount, String paymentType, Order order);
    public Payment createPayment(int amount, PaymentType paymentType, int orderId);
    public Payment createPayment(int amount, String paymentType, int orderId);
}
