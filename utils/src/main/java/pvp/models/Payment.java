package pvp.models;

import pvp.models.abstractModels.PkModel;
import pvp.models.interfaces.Order;

public class Payment extends PkModel implements pvp.models.interfaces.Payment {
    private PaymentType paymentType;
    private int amount;
    private Order order;
    private int orderId;

    /**
     * Payment()
     * @param pk - pk of the payment.
     * @param amount - amount of the payment.
     * @param paymentType as a PaymentType- payment type of the payment.
     * @param order - order of the payment.
     */
    public Payment (Integer pk, int amount, PaymentType paymentType, Order order) {
        super(pk);
        this.amount = amount;
        this.paymentType = paymentType;
        this.order = order;
    }

    /**
     * Payment()
     * @param pk - pk of the payment.
     * @param amount - amount of the payment.
     * @param paymentType as a String - payment type of the payment.
     * @param order - order of the payment.
     */
    public Payment (Integer pk, int amount, String paymentType, Order order) {
        super(pk);
        this.amount = amount;
        this.paymentType = PaymentType.valueOf(paymentType);
        this.order = order;
    }

    /**
     * Payment()
     * @param pk - pk of the payment.
     * @param amount - amount of the payment.
     * @param paymentType as a PaymentType - payment type of the payment.
     * @param orderId - order ID of the payment.
     */
    public Payment (Integer pk, int amount, PaymentType paymentType, int orderId) {
        super(pk);
        this.amount = amount;
        this.paymentType = paymentType;
        this.orderId = orderId;
    }

    /**
     * Payment()
     * @param pk - pk of the payment.
     * @param amount - amount of the payment.
     * @param paymentType as a String - payment type of the payment.
     * @param orderId as an int - order ID of the payment.
     */
    public Payment (Integer pk, int amount, String paymentType, int orderId) {
        super(pk);
        this.amount = amount;
        this.paymentType = PaymentType.valueOf(paymentType);
        this.orderId = orderId;
    }

@Override
public String toString() { return "amount: " + this.amount + "Type: " + this.paymentType; }
    @Override
    public PaymentType getPaymentType() {
        return this.paymentType;
    }

    @Override
    public int getPaymentTypeAsId() {
        return this.paymentType.ordinal();
    }

    /**
     * @param amount
     */
    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * @param order
     */
    @Override
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * @param id
     */
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
        try {
            if (this.order != null) {
                return this.order.getPk();
            }
        } catch (Exception e) {}
        return this.orderId;
    }

    @Override
    public int getAmount() {
        return this.amount;
    }
}