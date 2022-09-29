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

    private List<Product> searchedProducts = new ArrayList<Product>();
    Product banana = new pvp.models.Product(10, 30, "Banana", "219873");

    public void setSearchedProducts(List<Product> products){
        searchedProducts = products;
        searchedProducts.add(banana);
    }

    public void accept(ActionEvent event) {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();

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

        if (searchedProducts != null){prodTableView.getItems().addAll(searchedProducts);
        }
       searchedProducts.add(new pvp.models.Product(99,23, "Banana", "asd"));
        prodTableView.getItems().addAll(searchedProducts);

        prodTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue<? extends Product> observableValue, Product product, Product t1) {
                curretProd = prodTableView.getSelectionModel().getSelectedItem().getName();
                prodLabel.setText(curretProd);

            }
        });
    }
}
