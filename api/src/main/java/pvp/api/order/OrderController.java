package pvp.api.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pvp.api.payments.PaymentLineService;

import java.util.List;


@RestController
@RequestMapping("api/orders")
public class OrderController {

    private final OrderService orderService;
    private final PaymentLineService paymentLineService;

    @Autowired
    public OrderController(OrderService orderService, PaymentLineService paymentLineService) {
        this.orderService = orderService;
        this.paymentLineService = paymentLineService;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return this.orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getNoteById(@PathVariable(value = "id") int id){
        return orderService.findById(id);
    }

    @GetMapping("/incomplete")
    public List<Order> getIncompleteOrders() {
        return this.orderService.getIncompleteOrders();
    }

    @PostMapping
    public int addNewOrder(@RequestBody Order order) {
        int savedOrderid = this.orderService.addNewOrder(order);
        order.getPayments().forEach(payment -> {
            payment.setOrderId(savedOrderid);
            this.paymentLineService.addNewPaymentLine(payment);
        });
        return savedOrderid;
    }

}
