package pvp.cashier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminApplication extends Application {
    

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader listLoader = new FXMLLoader(getClass().getResource("Admin.fxml"));
        Parent list = listLoader.load();
        
        Stage AdminStage = new Stage();
        AdminStage.setScene(new Scene(list));

        AdminStage.setX(800);
        AdminStage.setY(200);

        AdminStage.show();
    }

    public static void main(final String[] args) {
        launch(args);
    }
}