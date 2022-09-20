package pvp.cashier.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collections;
import java.util.UUID;

import pvp.models.User;

public class Order extends pvp.models.Order {
    private final ObservableList<String> list;
    private ObservableList<String> selected;

    public Order() {
        super(1, 10, Collections.emptySet(), new User(1, UUID.randomUUID(), "Test user"), Collections.emptySet());
        list = FXCollections.observableArrayList();
    }


    public void addMessage(String msg){
        list.add(msg);
    }

    public ObservableList<String> getMessages(){
        return list;
    }

    public ObservableList<String> getSelectedMessages(){
        return selected;
    }

    public void setSelected(ObservableList<String> selected) {
        this.selected = selected;
    }
}
