package pvp.cashier.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;


@XmlRootElement(name = "result")
@XmlAccessorType (XmlAccessType.FIELD)
public class CardReader implements Serializable {
    private static final long serialVersionUID = 1L;
    private String bonusCardNumber;
    private String bonusState;
    private String paymentCardNumber;
    private String paymentState;
    private String paymentCardType;

    public CardReader() {
        super();
    }

    public CardReader(String bonusCardNumber, String bonusState,
                      String paymentCardNumber, String paymentState, String paymentCardType) {
        super();
        this.bonusCardNumber = bonusCardNumber;
        this.bonusState = bonusState;
        this.paymentCardNumber = paymentCardNumber;
        this.paymentState = paymentState;
        this.paymentCardType = paymentCardType;
    }
}
