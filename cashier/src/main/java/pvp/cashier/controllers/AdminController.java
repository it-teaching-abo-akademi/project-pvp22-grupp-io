package pvp.cashier.controllers;

import com.dlsc.formsfx.model.structure.DateField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import pvp.cashier.models.Order;
import pvp.models.interfaces.OrderLine;
import pvp.models.interfaces.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class AdminController implements Initializable {

    HashMap<String, String> filterArgs;

    @FXML
    private TextField customerInput;
    @FXML
    public TableView<Product> productView;
    @FXML
    private TextField ageInput;
    @FXML
    private DateField timeInput;
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
    @FXML
    private DatePicker timeInputStart;
    @FXML
    private DatePicker timeInputEnd;

    private List<Product> searchedProducts = new ArrayList<Product>();
    private Order order;
    private ArrayList<Product> productArrayList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filterArgs = new HashMap<String, String>();
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
        productArrayList = new ArrayList<>();
        String urlArgs = "";
        for (Map.Entry me : filterArgs.entrySet()) {
            urlArgs = urlArgs + me.getKey() + "=" + me.getValue() + ",";
        }
        searchedProducts = new ArrayList<Product>();
        URL url = new URL("http://127.0.0.1:8080/api/products/?" + urlArgs);
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
            json.forEach(object -> {
                JSONObject element = (JSONObject) object;
                String name = element.optString("name", "");
                String sku = element.optString("sku", "");

                productArrayList.add(new pvp.models.Product(
                        element.getInt("pk"),
                        element.getInt("price"),
                        name,
                        sku,
                        element.getInt("sold_count")
                ));
                productView.getItems().setAll(productArrayList);
            });
        }
    }

    public void editPrice(ActionEvent actionEvent) throws IOException {
        String newPriceString = newPriceInput.getText();
        int amount = Integer.parseInt(newPriceString);

        JSONObject json = new JSONObject();

        Product productToEdit = productView.getSelectionModel().getSelectedItem();
        productToEdit.setPrice(amount);

        json.put("price", productToEdit.getPrice());
        json.put("pk", productToEdit.getPk());
        json.put("sku", productToEdit.getSku());
        json.put("name", productToEdit.getName());

        URL postURL = new URL("http://localhost:8080/api/products");
        HttpURLConnection httpURLConnection = (HttpURLConnection) postURL.openConnection();
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        OutputStream os = httpURLConnection.getOutputStream();
        os.write(json.toString().getBytes());
        os.flush();
        os.close();
        System.out.println(httpURLConnection.getResponseCode());
        showProducts();
    }

    public void limitTime(ActionEvent actionEvent) {
        LocalDate newTimeStartString = timeInputStart.getValue();
        LocalDate newTimeEndString = timeInputEnd.getValue();
        if (filterArgs.containsKey("startTime")) {
            filterArgs.remove("startTime");
        }
        if (filterArgs.containsKey("endTime")) {
            filterArgs.remove("endTime");
        }
        if (newTimeStartString != null){
            filterArgs.put("startTime", newTimeStartString.toString());
        }
        if (newTimeEndString != null) {
            filterArgs.put("endTime", newTimeEndString.toString());
        }
        System.out.println(filterArgs);
    }

    public void limitAge(ActionEvent actionEvent) {
        String newAgeString = ageInput.getText();
        filterArgs.put("age", newAgeString);
    }

    public void limitMale(ActionEvent actionEvent) {
        filterArgs.put("gender", "male");
    }

    public void limitSexBoth(ActionEvent actionEvent) {
        filterArgs.put("gender", "both");

    }

    public void limitFemale(ActionEvent actionEvent) {
        filterArgs.put("gender", "female");
    }

    public void limitCustomer(ActionEvent actionEvent) {
        String newCustomerString = customerInput.getText();
        filterArgs.put("customer", newCustomerString);
    }
}
