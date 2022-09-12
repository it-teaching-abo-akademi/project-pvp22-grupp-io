package pvp.cashier;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import pvp.cashier.models.Order;

import java.util.List;

public class CustomerApplication {

    @FXML
    ListView<String> list;

    void setModel(Order model) {

        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //sets the selected items of the list to the model
        model.setSelected(list.getSelectionModel().getSelectedItems());

        //listen to changes in model, and respond
        model.getMessages().addListener(
                (ListChangeListener<String>) c -> {
                    c.next();
                    addElements(c.getAddedSubList());
                }
        );
    }

    private void addElements(List<? extends String> msgList){

        for(String msg : msgList){
            list.getItems().add(msg);
        }
    }
}
