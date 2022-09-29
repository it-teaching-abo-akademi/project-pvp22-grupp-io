package pvp.cashier.controllers;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONObject;
import pvp.cashier.models.Order;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pvp.models.interfaces.Product;
import pvp.cashier.controllers.ProductPopupController;


public class MainController {
    @FXML
    private Button SaveOrderButton;
    @FXML ListView<String> selected;
    private List<Product> searchedProducts = new ArrayList<Product>();
    @FXML
    private TextField skuInput;

    public void setModel(Order model) {

        //listen to changes in model, and respond
        model.getSelectedMessages().addListener(
                (ListChangeListener<String>) c -> {
                    c.next();
                    removeElements(c.getRemoved());
                    addElements(c.getAddedSubList());
                }
        );
    }

    @FXML
    private void doSearch(ActionEvent event) throws IOException {
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
                System.out.println(element);
                String name = (String) element.get("name");
                String sku = (String) element.get("sku");
                if (name == null) {
                    name = "";
                }
                if (sku == null) {
                    sku = "";
                }

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

    private void removeElements(List<? extends String> msgList){

        for(String msg : msgList){
            selected.getItems().remove(msg);
        }
    }

    private void addElements(List<? extends String> msgList){

        for(String msg : msgList){
            selected.getItems().add(msg);
        }
    }

    @FXML
    private void openProductList() throws IOException{
        FXMLLoader prodListLoader = new FXMLLoader(getClass().getResource("ProductPopup.fxml"));
        Parent prod = prodListLoader.load();

        ProductPopupController popupController = prodListLoader.getController();
        popupController.setSearchedProducts(searchedProducts);

        Stage prodListStage = new Stage();
        prodListStage.setScene(new Scene(prod));
        prodListStage.show();

    }
}