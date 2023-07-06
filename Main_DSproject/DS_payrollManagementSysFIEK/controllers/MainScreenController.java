package controllers;

import Utils.SessionManager;
import components.ErrorPopupComponent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Staff;
import repositories.StaffRepo;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    public static final String VIEW_PATH = "../Views";
    public static final String STAFF_DETAILS_VIEW = "staffDetails";
    public static final String MESSENGER_VIEW = "messenger";
    public static final String STAFF_LIST_VIEW = "staffList";
    private ChildController childController = null;
    @FXML
    private VBox contentPage;
    @FXML
    private Button addStaff;
    @FXML
    private ComboBox<String> message_professor;
    @FXML
    private Button oneToOneBtn;

    public void setView(String view) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(this.viewPath(view)));
        Pane pane = loader.load();
        ChildController controller = loader.getController();
        setView(view, pane, controller);
    }

    public void setView(String view, Pane pane, ChildController controller) throws Exception {

        switch (view) {
            case STAFF_DETAILS_VIEW:
                contentPage.setAlignment(Pos.TOP_CENTER);
                break;
            case STAFF_LIST_VIEW:
                contentPage.setAlignment(Pos.TOP_LEFT);
                break;
            case MESSENGER_VIEW:
                contentPage.setAlignment(Pos.TOP_RIGHT);
                break;
            default:
                throw new Exception("ERR_VIEW_NOT_FOUND");
        }
        this.childController = controller;
        this.childController.setParentController(this);
        contentPage.getChildren().clear();
        contentPage.getChildren().add(pane);
        VBox.setVgrow(pane, Priority.ALWAYS);
    }

    private String viewPath(String view) {
        return VIEW_PATH + "/" + view + ".fxml";
    }

    @FXML
    private void addStaffClick(ActionEvent event) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../Views/staffDetails.fxml"));
            Pane pane = loader.load();
            StaffDetailsController controller = loader.getController();
            //controller.setModel(new Staff());
            controller.setBtnName("Insert");
            this.setView(STAFF_DETAILS_VIEW, pane, controller);
        } catch (Exception ex) {
            ErrorPopupComponent.show(ex);
            ex.printStackTrace();
        }

    }

    public void disableStaffBtn() {
        this.addStaff.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<String> professors = FXCollections.observableArrayList(StaffRepo.findProfessors());
            message_professor.setItems(professors);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void startOneToOne(ActionEvent e) throws Exception {

            /*
            try{
            SessionManager.staffSelected = StaffRepo.findByName(message_professor.getValue());
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../Views/messenger.fxml"));
            Parent parent = loader.load();
            Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            primaryStage.setScene(new Scene(parent));
*/

      /*  SessionManager.staffSelected = StaffRepo.findByName(message_professor.getValue());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../Views/messenger.fxml"));
        Parent parent = loader.load();
        Stage primaryStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        primaryStage.setScene(new Scene(parent));
*/
        try {
            SessionManager.staffSelected = StaffRepo.findByName(message_professor.getValue());
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../Views/messenger.fxml"));
            Pane pane = loader.load();
            MessengerController controller = loader.getController();
            //controller.setModel(new Staff());
            // controller.setBtnName("Insert");
            this.setView(MESSENGER_VIEW, pane, controller);

        } catch (Exception ex) {
            ErrorPopupComponent.show(ex);
            ex.printStackTrace();
        }

    }

    @FXML
    public void startGroupChat(ActionEvent e) throws Exception {
        try {
            SessionManager.staffSelected = null;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../Views/messenger.fxml"));
            Pane pane = loader.load();
            MessengerController controller = loader.getController();
            //controller.setModel(new Staff());
            // controller.setBtnName("Insert");
            this.setView(MESSENGER_VIEW, pane, controller);

        } catch (Exception ex) {
            ErrorPopupComponent.show(ex);
            ex.printStackTrace();
        }
    }
}



