package pvp.cashier.controllers;


import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import pvp.models.interfaces.Order;
import pvp.models.interfaces.OrderLine;
import pvp.models.interfaces.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController implements Initializable {

    @FXML
    private TableView<OrderLine> customerProdView;
    @FXML
    private TableColumn<OrderLine, String> nameColumn;
    @FXML
    private TableColumn<OrderLine, Integer> amountColumn;
    @FXML
    private TableColumn<OrderLine, String> priceColumn;
    @FXML
    private TableColumn<OrderLine, String> discountColumn;
    @FXML
    private TextField orderTotal;
    @FXML
    private TextField leftToPay;
    private pvp.models.interfaces.Order order;

    /**
     * initialize()
     *
     * Connects the cells of the table in the front-end to their variables on the back-end.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        amountColumn.setCellValueFactory(param -> {
            ObservableValue<Integer> q = new ReadOnlyObjectWrapper<Integer>(param.getValue().getQuantity());
            return q;
        });
        priceColumn.setCellValueFactory(param -> {
                    ObservableValue<String> q = new ReadOnlyObjectWrapper<String>(Double.toString((param.getValue().getTotalPrice()) * .010) + "€");
                    return q;
                });

        discountColumn.setCellValueFactory(param -> {
            OrderLine orderline = param.getValue();
            Product product = orderline.getProduct();
            ObservableValue<String> q = new ReadOnlyObjectWrapper<String>(Double.toString((product.getPrice() * orderline.getQuantity() - orderline.getTotalPrice())*0.010) + "€");
            return q;
        });
        nameColumn.setCellValueFactory(param -> {
            String name = param.getValue().getProduct().getName() + "(" + param.getValue().getProduct().getSku() + ")";
            ObservableValue<String> q = new ReadOnlyObjectWrapper<String>(name);
            return q;
        });
    }

    public void setModel(Order model) {
        order = model;
    }

    public void updateOrderLines(){
        customerProdView.getItems().setAll(this.order.getOrderLines());
        orderTotal.setText(String.valueOf(this.order.getTotalPrice() * 0.0100) + "€");
        leftToPay.setText(String.valueOf((this.order.getTotalPrice() - this.order.getTotalPaidAmount()) * 0.0100) + "€");
    }
}