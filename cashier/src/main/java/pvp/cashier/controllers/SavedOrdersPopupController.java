package pvp.cashier.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import pvp.cashier.models.Order;
import pvp.models.interfaces.Product;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SavedOrdersPopupController implements Initializable {
    @FXML
    private TableColumn priceColumn;
    @FXML
    private TableColumn productsColumn;
    @FXML
    private TableColumn paymentsColumn;
    @FXML
    private TableColumn userIdColumn;
    @FXML
    private Label orderLabel;
    private int currentOrder;
    @FXML
    private  AnchorPane anchorPane;
    @FXML
    private TableView<Order> orderTableView;
    @FXML
    private Button acceptbutton;

    private MainController mainController;

    protected void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setSearchedOrders(List<Order> orders){
        orderTableView.getItems().addAll(orders);

    }

    public void accept(ActionEvent event) {
        Order selectedOrder = orderTableView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            //this.mainController.addSelectedOrder(selectedOrder);
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.close();
        }
        else {
            this.orderLabel.setText("Please, select an order!");
        }

    }

    private void createTableView(){
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        productsColumn.setCellValueFactory(new PropertyValueFactory<>("orderLines" ));
        paymentsColumn.setCellValueFactory(new PropertyValueFactory<>("payments" ));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userID" ));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createTableView();
        orderTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Order>() {
            @Override
            public void changed(ObservableValue<? extends Order> observableValue, Order order, Order t1) {
                currentOrder = orderTableView.getSelectionModel().getSelectedItem().getTotalPrice();
                orderLabel.setText(String.valueOf(currentOrder));
            }
        });
    }
}
