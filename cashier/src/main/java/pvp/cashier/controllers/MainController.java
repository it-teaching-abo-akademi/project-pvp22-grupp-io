package pvp.cashier.controllers;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import org.json.JSONArray;
import org.json.JSONObject;
import pvp.cashier.models.Order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javafx.stage.Popup;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Popup;
import pvp.models.interfaces.Product;


public class MainController {

    @FXML ListView<String> selected;

    private List<Product> searchedProducts;

    @FXML
    private TextField skuInput;

    @FXML
    private TextField skuInput2;


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


        } else {
            System.out.println("GET request not worked");
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

    private EventHandler<ActionEvent> popupevent = new EventHandler<ActionEvent>() {
        private Window skuInput2;

        @Override
            public void handle(ActionEvent actionEvent) {
                Popup popup = new Popup();

                TilePane tilepane = new TilePane();
                Label label = new Label("This is a Popup");
                label.setStyle(" -fx-background-color: white;");
                popup.getContent().add(label);

                label.setMinWidth(80);
                label.setMinHeight(50);
                if (!popup.isShowing())
                    popup.show(this.skuInput2);
                else
                    popup.hide();
            }
        };
}