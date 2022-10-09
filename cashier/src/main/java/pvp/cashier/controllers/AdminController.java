package pvp.cashier.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import pvp.models.interfaces.OrderLine;
import pvp.models.interfaces.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.List;

public class AdminController implements Initializable {

    @FXML
    private Button ageLimitButton;
    @FXML
    private Button sexLimitButtonM;
    @FXML
    private Button sexLimitButtonNone;
    @FXML
    private Button sexLimitButtonF;
    @FXML
    private TextField ageInput;
    @FXML
    private Button periodLimitButton;
    @FXML
    private TextField timeInput;
    @FXML
    private TableView<Product> ProductView;
    @FXML
    private TableColumn<Product, Integer> itemSoldColumn;
    @FXML
    private TableColumn<Product, Integer> timeColumn;
    @FXML
    private TableColumn<Product, Integer> productColumn;
    @FXML
    private TableColumn<Product, Integer> pkColumn;
    @FXML
    private TableColumn<Product, Integer> priceColumn;
    @FXML
    private TableColumn<Product, Integer> skuColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        pkColumn.setCellValueFactory(new PropertyValueFactory<>("pk"));
        skuColumn.setCellValueFactory(new PropertyValueFactory<>("sku"));
    }
}
