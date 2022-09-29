package pvp.cashier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pvp.cashier.controllers.CustomerController;
import pvp.cashier.controllers.MainController;
import pvp.cashier.models.Order;

public class MainApplication extends Application {

    private Order model;

    @Override
    public void start(Stage primaryStage) throws Exception{
        model = new Order();

        FXMLLoader listLoader = new FXMLLoader(getClass().getResource("Customer.fxml"));
        Parent list = listLoader.load();
        CustomerController listController = listLoader.getController();
        listController.setModel(model);

        FXMLLoader selectedLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent selected = selectedLoader.load();
        MainController selectedController = selectedLoader.getController();
        selectedController.setModel(model);

        primaryStage.setScene(new Scene(list));
        Stage secondaryStage = new Stage();
        secondaryStage.setScene(new Scene(selected));

        addMessages();
        primaryStage.show();
        secondaryStage.show();
    }

    private void addMessages() {

        int counter = 0;
        while(counter < 15) {
            model.addMessage("message number "+ counter++);
        }
    }

    public static void main(final String[] args) {
        launch(args);
    }
}