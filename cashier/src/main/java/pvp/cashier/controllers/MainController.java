package pvp.cashier.controllers;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import pvp.cashier.models.Order;

import java.util.List;

public class MainController {
    
    @FXML ListView<String> selected;

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
}
