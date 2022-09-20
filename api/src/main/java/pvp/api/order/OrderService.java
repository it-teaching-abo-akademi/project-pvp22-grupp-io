package pvp.api.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderDataAccessService orderDataAccessService;

    List<Order> getAllOrders() {
        return orderDataAccessService.selectAllOrders();
    }

    Order findById(int id) { return orderDataAccessService.getOrderById(id); }

    int addNewOrder(Order order) {
        return orderDataAccessService.insertOrder(order);
    }
}