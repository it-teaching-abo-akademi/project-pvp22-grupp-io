package pvp.models.interfaces;

import pvp.models.PaymentType;

import java.util.Optional;
import java.util.Set;

public interface Order extends PkModel {
    // Base interface for the orders
    // Comment 2

    public void updateTotalPrice();
    public int getTotalPrice();
    public int getTotalPaidAmount();
    public Set<OrderLine> getOrderLines();
    public boolean isComplete();
    public Set<Payment> getPayments();
    public void setIsComplete(boolean isComplete);
    public void addOrderLine(OrderLine orderLine);
    public void addProduct(Product product);
    public Optional<OrderLine> getOrderLineById(int id);
    public void removeOrderLine(OrderLine orderLine);
    public void removeOrderLine(int orderLineId);
    public void setUser(User user);
    public void setUserId(int id);
    public User getUser();
    public int getUserId();
    public void createPayment(int amount, PaymentType paymentType);
    public void createPayment(int amount, String paymentType);
    public void createPayment(Integer pk, int amount, String paymentType);

    public void setPk(int pk);
}
