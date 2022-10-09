package pvp.cashier.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import pvp.models.interfaces.OrderLine;
import pvp.models.interfaces.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    public Button ageLimitButton;
    @FXML
    public Button sexLimitButtonM;
    @FXML
    public Button sexLimitButtonNone;
    @FXML
    public Button sexLimitButtonF;
    @FXML
    public TextField ageInput;
    @FXML
    public Button periodLimitButton;
    @FXML
    public TextField timeInput;
    @FXML
    public TableColumn itemSoldColumn;
    @FXML
    public TableColumn timeColumn;
    @FXML
    public TableColumn productColumn;
    @FXML
    private TableView ProductView;

    @FXML
    private TableColumn<OrderLine, Integer> pkColumn;
    @FXML
    private TableColumn<OrderLine, Integer> priceColumn;
    @FXML
    private TableColumn<OrderLine, Integer> skuColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price" ));
        pkColumn.setCellValueFactory(new PropertyValueFactory<>("pk" ));
        skuColumn.setCellValueFactory(new PropertyValueFactory<>("sku" ));
    }

}
