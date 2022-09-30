package pvp.cashier.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pvp.models.Order;
import pvp.models.interfaces.Product;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProductPopupController implements Initializable {
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn priceColumn;
    @FXML
    private TableColumn skuColumn;
    @FXML
    private TableColumn pkColumn;
    @FXML
    private Label prodLabel;
    private String curretProd;
    @FXML
    private  AnchorPane anchorPane;
    @FXML
    private TableView<Product> prodTableView;
    @FXML
    private Button acceptbutton;

    private Order order;

    protected void setOrder(Order order) {
        this.order = order;
    }

    public void setSearchedProducts(List<Product> products){
        prodTableView.getItems().addAll(products);

    }

    public void accept(ActionEvent event) {
        Product selectedProduct = prodTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            this.order.addProduct(selectedProduct);
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.close();
        }
        else {
            this.prodLabel.setText("Please, select a product!");
        }

    }

    private void createTableView(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price" ));
        pkColumn.setCellValueFactory(new PropertyValueFactory<>("pk" ));
        skuColumn.setCellValueFactory(new PropertyValueFactory<>("sku" ));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createTableView();
        prodTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue<? extends Product> observableValue, Product product, Product t1) {
                curretProd = prodTableView.getSelectionModel().getSelectedItem().getName();
                prodLabel.setText(curretProd);
            }
        });
    }
}
