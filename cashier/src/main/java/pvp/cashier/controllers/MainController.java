package pvp.cashier.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.json.JSONArray;
import org.json.JSONObject;
import pvp.cashier.models.Order;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import pvp.models.interfaces.OrderLine;
import pvp.models.interfaces.Product;


public class MainController implements Initializable {
    @FXML
    private Button SaveOrderButton;
    @FXML ListView<String> selected;
    private List<Product> searchedProducts = new ArrayList<Product>();
    @FXML
    private TextField skuInput;
    private Order order;
    @FXML
    private TableView<OrderLine> prodTableView;
    @FXML
    private TableColumn<OrderLine, String> NameColumn;
    @FXML
    private TableColumn<OrderLine, Integer> amountColumn;
    @FXML
    private TableColumn<OrderLine, Integer> priceColumn;
    @FXML
    private TableColumn<OrderLine, Integer> discountColumn;

    private CustomerController customerController;

    public void setModel(Order model) {
        order = model;
    }

    public void setCustomerController(CustomerController customerController){
        this.customerController = customerController;
    }

    @FXML
    private void doSearch(ActionEvent event) throws IOException {
        searchedProducts = new ArrayList<Product>();
        URL url = new URL("http://127.0.0.1:8080/api/products/search/" + this.skuInput.getText());
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in .readLine()) != null) {
                response.append(inputLine);
            } in .close();

            JSONArray json = new JSONArray(response.toString());
            json.forEach(object -> {
                JSONObject element = (JSONObject) object;
                String name = element.optString("name", "");
                String sku = element.optString("sku", "");

                searchedProducts.add(new pvp.models.Product(
                        element.getInt("pk"),
                        element.getInt("price"),
                        name,
                        sku
                ));
            });
            try {
                openProductList();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("GET request not worked");
            openProductList();
        }


    }

    protected void addSelectedProduct(Product product){
        order.addProduct(product);
        updateOrderLines();
    }

    private void updateOrderLines(){
        prodTableView.getItems().setAll(this.order.getOrderLines());
        customerController.updateOrderLines();
    }

    @FXML
    private void openProductList() throws IOException{
        FXMLLoader prodListLoader = new FXMLLoader(getClass().getResource("ProductPopup.fxml"));
        Parent prod = prodListLoader.load();

        ProductPopupController popupController = prodListLoader.getController();
        popupController.setSearchedProducts(searchedProducts);
        popupController.setMainController(this);

        Stage prodListStage = new Stage();
        prodListStage.setScene(new Scene(prod));
        prodListStage.show();

    }

    @Override
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
        NameColumn.setCellValueFactory(param -> {
            String name = param.getValue().getProduct().getName() + "(" + param.getValue().getProduct().getSku() + ")";
            ObservableValue<String> q = new ReadOnlyObjectWrapper<String>(name);
            return q;
        });

        prodTableView.setRowFactory(tableView -> {
            final TableRow<OrderLine> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();
            MenuItem removeItem = new MenuItem("Delete");
            removeItem.setOnAction(e -> {
                order.removeOrderLine(row.getItem());
                updateOrderLines();
            });

            rowMenu.getItems().addAll(removeItem);
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(rowMenu)
                            .otherwise((ContextMenu)null));
            return row;
        });

    }
}