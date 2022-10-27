package pvp.api.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderDataAccessService orderDataAccessService;

    /**
     * Returns all orders.
     */
    List<Order> getAllOrders() {
        return orderDataAccessService.selectAllOrders();
    }

    /**
     * Returns all incomplete orders
     */
    List<Order> getIncompleteOrders() {
        return orderDataAccessService.selectIncompleteOrders();
    }

    /**
     * Locates order by its ID.
     * @param id - id or the order.
     */
    Order findById(int id) { return orderDataAccessService.getOrderById(id); }

    /**
     * Adds new order to the service.
     * @param order - the order to be added.
     */
    int addNewOrder(Order order) {
        return orderDataAccessService.insertOrder(order);
    }
}
