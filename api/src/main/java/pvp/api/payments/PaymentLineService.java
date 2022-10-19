package pvp.api.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pvp.models.interfaces.Payment;
import java.util.List;

@Service
public class PaymentLineService {
    private final PaymentLineDataAccessService paymentLineDataAccessService;

    @Autowired
    public PaymentLineService(PaymentLineDataAccessService paymentLineDataAccessService) {
        this.paymentLineDataAccessService = paymentLineDataAccessService;
    }

    List<Payment> getAllPayments() {
        return paymentLineDataAccessService.selectAllPaymentLines();
    }

    Payment findById(String id) { return paymentLineDataAccessService.getPaymentLineById(id); }

    public int addNewPaymentLine(Payment payment) {
        return paymentLineDataAccessService.insertPaymentLine(payment);
    }
}
