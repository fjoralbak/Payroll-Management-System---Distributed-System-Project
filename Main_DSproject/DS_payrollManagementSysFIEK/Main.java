import Utils.Security;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import models.LoginInfo;
import client.HttpClient;
import models.Staff;
import repositories.StaffRepo;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {

//        Parent parent = FXMLLoader.load(getClass().getResource("/Views/messenger.fxml"));

       // Parent parent = FXMLLoader.load(getClass().getResource("/Views/messenger.fxml"));
       //Parent parent = FXMLLoader.load(getClass().getResource("/Views/main-screen.fxml"));

//        Scene scene = new Scene(parent);
//        stage.setScene(scene);
//        stage.setTitle("Client");
//        stage.show();

       // Parent parent = FXMLLoader.load(getClass().getResource("/Views/messenger.fxml"));
        Parent parent = FXMLLoader.load(getClass().getResource("/Views/login.fxml"));

        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Interactive system for booking rental cars");
        stage.show();
        stage.setOnCloseRequest(event ->
        {
            event.consume();//consume the event
            exit(stage);
        });
 }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
    private void exit(Stage stage) {
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Exit");
        alert.setContentText("Are you sure you want to exit?");
        if(alert.showAndWait().get()== ButtonType.OK){
            stage.close();
        }
    }
}
