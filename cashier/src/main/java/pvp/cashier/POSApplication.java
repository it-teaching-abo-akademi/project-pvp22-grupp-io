package pvp.cashier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pvp.cashier.controllers.CustomerController;
import pvp.cashier.controllers.CashierController;
import pvp.cashier.models.Order;

public class POSApplication extends Application {

    @Override
    public void start(Stage cashierStage) throws Exception{
        Stage customerStage = new Stage();
        Order model = new Order();

        FXMLLoader CustomerLoader = new FXMLLoader(getClass().getResource("Customer.fxml"));
        Parent customer = CustomerLoader.load();
        CustomerController Customercontroller = CustomerLoader.getController();

        FXMLLoader CashierLoader = new FXMLLoader(getClass().getResource("Cashier.fxml"));
        Parent cashier = CashierLoader.load();
        CashierController cashierController = CashierLoader.getController();
        cashierController.setCustomerController(Customercontroller);
        cashierController.setModel(model);

        cashierStage.setScene(new Scene(cashier));
        customerStage.setScene(new Scene(customer));

        customerStage.setX(150);
        customerStage.setY(200);

        cashierStage.setX(800);
        cashierStage.setY(200);

        cashierStage.show();
        customerStage.show();
    }

    public static void main(final String[] args) {
        launch(args);
    }
}