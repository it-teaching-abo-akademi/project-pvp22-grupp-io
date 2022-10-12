package pvp.api.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
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
        return this.orderService.addNewOrder(order);
    }

}
