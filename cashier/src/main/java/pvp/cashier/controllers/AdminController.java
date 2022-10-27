package pvp.cashier.controllers;


import com.dlsc.formsfx.model.structure.DateField;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import pvp.models.interfaces.Product;
import org.json.JSONArray;
import org.json.JSONObject;
import pvp.cashier.models.Order;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class AdminController implements Initializable {


    JSONObject filterArgs;

    @FXML
    public TextField skuInput;
    @FXML
    private TextField customerInput;
    @FXML
    public TableView<Product> productView;
    @FXML
    private TextField ageInput;
    @FXML
    private TableColumn<Product, Integer> itemSoldColumn;
    @FXML
    private TableColumn<Product, Integer> productColumn;
    @FXML
    private TableColumn<Product, Integer> pkColumn;
    @FXML
    private TableColumn<Product, String> priceColumn;
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

    /**
     * initialize()
     *
     * Connects the cells of the table in the front-end to their variables on the back-end.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filterArgs = new JSONObject();
        productColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        //priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellValueFactory(param -> {
            ObservableValue<String> q = new ReadOnlyObjectWrapper<String>(priceRounder(param.getValue().getPrice()));
            return q;
        });
        pkColumn.setCellValueFactory(new PropertyValueFactory<>("pk"));
        skuColumn.setCellValueFactory(new PropertyValueFactory<>("sku"));
        itemSoldColumn.setCellValueFactory(new PropertyValueFactory<>("soldCount"));
        //timeColumn.setCellValueFactory(new PropertyValueFactory<>("timePeriod????????"));

        try {
            showProducts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for displaying products with additional information.
     * Shows all products when the AdminApplication starts, limits products and
     * information based of filterArgs, used by the different limit methods
     */
    private void showProducts() throws IOException {
        productArrayList = new ArrayList<>();
        searchedProducts = new ArrayList<Product>();
        URL url = new URL("http://127.0.0.1:8080/api/products/");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("filters", filterArgs.toString());
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
                productArrayList.add(pvp.models.Product.getObjectFromJson(element));
            });
        }
        productView.getItems().setAll(productArrayList);
    }

    /**
     * Allows the admin to edit the prices of products in cents as the
     * newPriceString
     */
    public void editPrice(ActionEvent actionEvent) throws IOException {
        String newPriceString = newPriceInput.getText();
        int amount = (int) (Float.parseFloat(newPriceString) * 100);

        Product productToEdit = productView.getSelectionModel().getSelectedItem();
        productToEdit.setPrice(amount);

        JSONObject json = productToEdit.getJsonOfObject();

        URL postURL = new URL("http://localhost:8080/api/products");
        HttpURLConnection httpURLConnection = (HttpURLConnection) postURL.openConnection();
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        OutputStream os = httpURLConnection.getOutputStream();
        os.write(json.toString().getBytes());
        os.flush();
        os.close();
        httpURLConnection.getResponseCode();
        showProducts();
    }

    public void limitTime(ActionEvent actionEvent) {
        LocalDate newTimeStartString = timeInputStart.getValue();
        LocalDate newTimeEndString = timeInputEnd.getValue();
        if (filterArgs.has("startTime")) {
            filterArgs.remove("startTime");
        }
        if (filterArgs.has("endTime")) {
            filterArgs.remove("endTime");
        }
        if (newTimeStartString != null){
            filterArgs.put("startTime", newTimeStartString.toString());
        }
        if (newTimeEndString != null) {
            filterArgs.put("endTime", newTimeEndString.toString());
        }
    }

    public void doSearch(ActionEvent actionEvent) throws IOException {
        showProducts();
    }

    public void limitAge(ActionEvent actionEvent) {
        String newAgeString = ageInput.getText();
        if (newAgeString.equals("")) {
            filterArgs.remove("age");
            return;
        }
        filterArgs.put("age", newAgeString);
    }

    public void limitMale(ActionEvent actionEvent) {
        filterArgs.put("sex", "MALE");
    }

    public void limitSexBoth(ActionEvent actionEvent) {
        if (filterArgs.has("sex")){
            filterArgs.remove("sex");
        }
    }

    public void limitFemale(ActionEvent actionEvent) {
        filterArgs.put("sex", "FEMALE");
    }

    public void limitCustomer(ActionEvent actionEvent) {
        String newCustomerString = customerInput.getText();
        if (newCustomerString.equals("")) {
            filterArgs.remove("customer");
            return;
        }
        filterArgs.put("customer", newCustomerString);
    }


    public void limitSku(ActionEvent actionEvent) {
        String newSkuString = skuInput.getText();
        if (newSkuString.equals("")) {
            filterArgs.remove("sku");
            return;
        }
        filterArgs.put("sku", newSkuString);
    }

    public String priceRounder(int price) {
        return String.format("$%.2f€ ", price * 0.01);
    }
}
