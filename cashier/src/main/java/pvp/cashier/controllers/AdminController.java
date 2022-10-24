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
import pvp.cashier.models.Order;
import pvp.models.interfaces.OrderLine;
import pvp.models.interfaces.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.List;

public class AdminController implements Initializable {

    @FXML
    public TableView<Product> productView;
    @FXML
    private TextField ageInput;
    @FXML
    private TextField timeInput;
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
    @FXML
    private TextField newPriceInput;

    private List<Product> searchedProducts = new ArrayList<Product>();
    private Order order;
    private ArrayList<Product> productArrayList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        pkColumn.setCellValueFactory(new PropertyValueFactory<>("pk"));
        skuColumn.setCellValueFactory(new PropertyValueFactory<>("sku"));

        try {
            showProducts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showProducts() throws IOException {
        searchedProducts = new ArrayList<Product>();
        URL url = new URL("http://127.0.0.1:8080/api/products/");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONArray json = new JSONArray(response.toString());
            if (json.length() == 1) {
                JSONObject element = (JSONObject) json.get(0);
                String name = element.optString("name", "");
                String sku = element.optString("sku", "");

                productArrayList.add(new pvp.models.Product(
                        element.getInt("pk"),
                        element.getInt("price"),
                        name,
                        sku
                        /**
                        Items sold och time period saknar all form av implementation
                         */
                ));
                productView.getItems().setAll(productArrayList);
            }
        }
    }

    public void editPrice(ActionEvent actionEvent) {
        String newPriceString = newPriceInput.getText();
        int amount = Integer.parseInt(newPriceString);
        /**
         * Tror inte man kan använda sig av orderlines, koden skiter sig eftersom man
         * den behandlar productviews som object
         *
        productToEdit = productView.getSelectionModel().getSelectedItem();
        productToEdit.setUnitPrice(amount);
         */
    }



    public void limitTime(ActionEvent actionEvent) {
    }

    public void limitAge(ActionEvent actionEvent) {
    }

    public void limitMale(ActionEvent actionEvent) {
    }

    public void limitSexBoth(ActionEvent actionEvent) {
    }

    public void limitFemale(ActionEvent actionEvent) {
    }
}
