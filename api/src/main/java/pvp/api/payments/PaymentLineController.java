package pvp.api.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pvp.models.interfaces.Payment;
import java.util.List;


@RestController
@RequestMapping("api/payments")
public class PaymentLineController {

    private final PaymentLineService paymentLineService;

    @Autowired
    public PaymentLineController(PaymentLineService paymentLineService) {
        this.paymentLineService = paymentLineService;
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return this.paymentLineService.getAllPayments();
    }

    @GetMapping("/{id}")
    public Payment getNoteById(@PathVariable(value = "id") String id){
        return paymentLineService.findById(id);
    }

    @PostMapping
    public void addNewPaymentLine(@RequestBody Payment payment) {
        this.paymentLineService.addNewPaymentLine(payment);
    }

}
