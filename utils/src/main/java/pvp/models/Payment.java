package pvp.models;

import pvp.models.abstractModels.PkModel;
import pvp.models.interfaces.Order;

public class Payment extends PkModel implements pvp.models.interfaces.Payment {
    private PaymentType paymentType;
    private int amount;

    private Order order;
    private int orderId;


    public Payment (Integer pk, int amount, PaymentType paymentType, Order order) {
        super(pk);
        this.amount = amount;
        this.paymentType = paymentType;
        this.order = order;
        this.orderId = order.getPk();
    }

    public Payment (Integer pk, int amount, String paymentType, Order order) {
        super(pk);
        this.amount = amount;
        this.paymentType = PaymentType.valueOf(paymentType);
        this.order = order;
        this.orderId = order.getPk();
    }
    public Payment (Integer pk, int amount, PaymentType paymentType, int orderId) {
        super(pk);
        this.amount = amount;
        this.paymentType = paymentType;
        this.orderId = orderId;
    }
    public Payment (Integer pk, int amount, String paymentType, int orderId) {
        super(pk);
        this.amount = amount;
        this.paymentType = PaymentType.valueOf(paymentType);
        this.orderId = orderId;
    }


    @Override
    public PaymentType getPaymentType() {
        return this.paymentType;
    }

    @Override
    public int getPaymentTypeAsId() {
        return this.paymentType.ordinal();
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public void setOrderId(int id) {
        this.orderId = id;
    }

    @Override
    public Order getOrder() {
        return this.order;
    }

    @Override
    public int getOrderId() {
        if (this.order != null) {
            return this.order.getPk();
        }
        return this.orderId;
    }

    @Override
    public int getAmount() {
        return this.amount;
    }
}