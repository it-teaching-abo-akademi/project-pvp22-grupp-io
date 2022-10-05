package pvp.cashier.controllers;


import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import pvp.cashier.models.Order;
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
    private TableColumn<OrderLine, Integer> priceColumn;
    @FXML
    private TableColumn<OrderLine, Integer> discountColumn;

    @FXML
    private TextField orderTotal;

    @FXML
    private TextField leftToPay;
    private Order order;
    public void initialize(URL url, ResourceBundle resourceBundle) {
        amountColumn.setCellValueFactory(param -> {
            ObservableValue<Integer> q = new ReadOnlyObjectWrapper<Integer>(param.getValue().getQuantity());
            return q;
        });
        priceColumn.setCellValueFactory(param -> {
            ObservableValue<Integer> q = new ReadOnlyObjectWrapper<Integer>(param.getValue().getTotalPrice());
            return q;
        });
        discountColumn.setCellValueFactory(param -> {
            OrderLine orderline = param.getValue();
            Product product = orderline.getProduct();
            ObservableValue<Integer> q = new ReadOnlyObjectWrapper<Integer>(product.getPrice() * orderline.getQuantity() - orderline.getTotalPrice());
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
        orderTotal.setText(String.valueOf(this.order.getTotalPrice()));
        System.out.println(this.order.getTotalPrice());
        System.out.println(this.order.getTotalPaidAmount());
        leftToPay.setText(String.valueOf(this.order.getTotalPrice() - this.order.getTotalPaidAmount()));
    }
}