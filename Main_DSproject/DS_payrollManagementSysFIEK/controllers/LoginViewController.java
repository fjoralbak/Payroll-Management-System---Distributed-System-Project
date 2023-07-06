package controllers;

import java.util.ArrayList;
import java.util.Date;

import Utils.Authenticate;
import Utils.Notification;
import Utils.SessionManager;
import client.HttpClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.LoginInfo;
import models.Staff;
import repositories.StaffRepo;

public class LoginViewController {
    @FXML
    private TextField usernanme;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    @FXML
    private Button cancel;


    @FXML
    private void loginclicked (ActionEvent event) throws Exception {
        if (!usernanme.getText().isBlank() && !password.getText().isBlank()) {
            try {
                Staff staff = null;
                String emailF = usernanme.getText();
                String passwordF = password.getText();
                staff = HttpClient.sendLoginRequest(new LoginInfo(emailF,passwordF));
                if(staff!=null)
                {
                    SessionManager.user = staff;
                    SessionManager.lastLogin = new Date();
                    SessionManager.allStaff = (ArrayList<Staff>) HttpClient.getStaffRequest(staff);
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("../views/main-screen.fxml"));

                    Parent parent = loader.load();
                    MainScreenController controller = loader.getController();
                    controller.setView(MainScreenController.STAFF_LIST_VIEW);
                    if (!staff.isAdmin()) controller.disableStaffBtn();

                    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(parent);
                    primaryStage.setScene(scene);
                    primaryStage.setResizable(false);
                }
                else{
                    String errStr="";
                    ArrayList<Notification> errors = Authenticate.getNotifications();
                    for (int i = 0; i < errors.size(); i++) {
                        errStr+="* "+errors.get(i).getMsg();
                    }
                    Authenticate.clearNotificaitons();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText(errStr);
                    alert.showAndWait();
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Username and password fields are required!");
            alert.showAndWait();
        }


    }
    @FXML
    private void cancelclicked(ActionEvent event) throws Exception {
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Exit");
        alert.setContentText("Are you sure you want to leave ?");
        if(alert.showAndWait().get()==ButtonType.OK){
            Stage stage=(Stage) cancel.getScene().getWindow();
            stage.close();
        }
    }

}