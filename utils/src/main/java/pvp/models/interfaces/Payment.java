package pvp.models.interfaces;

import pvp.models.PaymentType;

public interface Payment extends PkModel {
    public PaymentType getPaymentType();
    public int getPaymentTypeAsId();
    public void setAmount(int amount);

    public void setOrder(Order order);
    public void setOrderId(int id);
    public Order getOrder();
    public int getOrderId();
    public int getAmount();

}
