package controllers.partials;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Staff;

import java.net.URL;
import java.util.ResourceBundle;

public class StaffCardController implements Initializable {
    @FXML
    Button showDetails;

    @FXML
    Button deleteButton;

    @FXML
    Button editButton;

    @FXML
    private Label name;
    @FXML
    private Label surname;
    @FXML
    private Label department;
    @FXML
    private Label SSN;
    @FXML
    private Label position;
    @FXML
    private Label salary;
    @FXML
    private Label adminOrProfessor;
    @FXML
    private ImageView img;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setStaff(Staff staff) {
        try {
            name.setText("Name: " + staff.getName());
            surname.setText("Surname: " +staff.getSurname());
            department.setText(staff.getDepartment());
            SSN.setText("SSN: " + staff.getSSN());
            position.setText("Position: "+staff.getPosition());
            salary.setText("Salary:"+staff.getSalary());
            adminOrProfessor.setText(staff.isAdmin()?"Admin":"Professor");
            Image image = new Image(getClass().getResource("../../resources/images/user-icon.png").toURI().toString());
            img.setImage(image);

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public void setOnViewDetailsAction(EventHandler<ActionEvent> showDetailsAc) {
        showDetails.setOnAction(showDetailsAc);
    }

    public void setOnEditAction(EventHandler<ActionEvent> editHandler) {
        editButton.setOnAction(editHandler);
    }

    public void setOnDeleteAction(EventHandler<ActionEvent> deleteHandler) {

        deleteButton.setOnAction(deleteHandler);

    }
}
