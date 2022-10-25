package pvp.api.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDataAccessService orderDataAccessService;

    List<Order> getAllOrders() {
        return orderDataAccessService.selectAllOrders();
    }

    List<Order> getIncompleteOrders() {
        return orderDataAccessService.selectIncompleteOrders();
    }

    Order findById(int id) { return orderDataAccessService.getOrderById(id); }

    int addNewOrder(Order order) {
        return orderDataAccessService.insertOrder(order);
    }
}
