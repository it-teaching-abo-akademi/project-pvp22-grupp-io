package pvp.cashier.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pvp.models.interfaces.OrderLine;
import pvp.models.interfaces.Product;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProductPopupController implements Initializable {
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, String> priceColumn;
    @FXML
    private TableColumn<Product, String> skuColumn;
    @FXML
    private TableColumn<Product, String> pkColumn;
    @FXML
    private Label prodLabel;
    @FXML
    private  AnchorPane anchorPane;
    @FXML
    private TableView<Product> prodTableView;
    private CashierController cashierController;
    private String currentProd;

    /**
     * initialize()
     *
     * Connects the cells of the table in the front-end to their variables on the back-end.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createTableView();
        prodTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue<? extends Product> observableValue, Product product, Product t1) {
                currentProd = prodTableView.getSelectionModel().getSelectedItem().getName();
                prodLabel.setText(currentProd);
            }
        });
    }

    protected void setMainController(CashierController cashierController) {
        this.cashierController = cashierController;
    }

    public void setSearchedProducts(List<Product> products){
        prodTableView.getItems().addAll(products);
    }

    /**
     * Adds the selected product to the current order
     */
    public void accept(ActionEvent event) {
        Product selectedProduct = prodTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            this.cashierController.addSelectedProduct(selectedProduct);
            Stage stage = (Stage) anchorPane.getScene().getWindow();
            stage.close();
        }
        else {
            this.prodLabel.setText("Please, select a product!");
        }
    }

    private void createTableView(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        //priceColumn.setCellValueFactory(new PropertyValueFactory<>("price" ));
        pkColumn.setCellValueFactory(new PropertyValueFactory<>("pk" ));
        skuColumn.setCellValueFactory(new PropertyValueFactory<>("sku" ));

        priceColumn.setCellValueFactory(param -> {
            ObservableValue<String> q = new ReadOnlyObjectWrapper<String>(Double.toString((param.getValue().getPrice()) *.010) + "???");
            return q;
        });

    }


}
