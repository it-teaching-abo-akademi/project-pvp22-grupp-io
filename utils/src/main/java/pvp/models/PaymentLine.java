package pvp.models;

import pvp.models.abstractModels.PkModel;

public class PaymentLine extends PkModel {
    private int price;
    private PaymentType paymentType;

    public PaymentLine(String pk, int price, PaymentType paymentType) {
        super(pk);
        this.price = price;
        this.paymentType = paymentType;
    }

    public int getPrice(){
        return this.price;
    }

    public PaymentType getPaymentType() {
        return this.paymentType;
    }

    public enum PaymentType {
        CASH,
        CARD
    }
}