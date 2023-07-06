package controllers;

import Utils.Authenticate;
import Utils.Notification;
import Utils.SessionManager;
import client.HttpClient;
import components.ErrorPopupComponent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import models.Staff;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StaffDetailsController extends ChildController {

    @FXML
    private TextField ssnField;
    @FXML
    private TextField firstNameField;
    @FXML
    private DatePicker birthdateField;
    @FXML
    private TextField streetHouseField;
    @FXML
    private ComboBox<String> cityField;
    @FXML
    private TextField zipCodeField;
    @FXML
    private ComboBox<String> positionField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField bankAccField;
    @FXML
    private RadioButton maleBtn;
    @FXML
    private RadioButton femaleBtn;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private ComboBox<String> departmentField;
    @FXML
    private TextField experienceField;
    @FXML
    private TextField salaryField;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label validationField;

    private String salt = "";
    private boolean isAdmin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> cityList =
                FXCollections.observableArrayList("Prishtinë", "Mitrovicë", "Vushtrri", "Gjakovë", "Prizren");
        cityField.setItems(cityList);

        ObservableList<String> degreeList =
                FXCollections.observableArrayList("Associate Degree", "Bachelor's Degree", "Master's Degree", "Doctoral Degree");
        cityField.setItems(degreeList);

        ObservableList<String> designationField =
                FXCollections.observableArrayList("Professor", "Assistant"); //mi shtu
        cityField.setItems(designationField);

        ObservableList<String> branchField =
                FXCollections.observableArrayList("IT"); //mi shtu
        cityField.setItems(branchField);
    }

    @FXML
    private void onCancelBtnClick(ActionEvent event) {
        try {
            parentController.setView(MainScreenController.STAFF_LIST_VIEW);
        } catch (Exception e) {
            ErrorPopupComponent.show(e);
        }
    }

    @FXML
    private void onSaveBtnClick(ActionEvent event) throws Exception {

        if (firstNameField.getText().isBlank() == false && passwordField.getText().isBlank() == false
                && confirmPasswordField.getText().isBlank() == false && emailField.getText().isBlank() == false) {
            if ((passwordField.getText().equals(confirmPasswordField.getText())) == false
            ) {
                validationField.setTextFill(Color.RED);
                validationField.setText("       Please confirm the password!");
            } else if (is_Valid_Password(passwordField.getText()) == false) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Pasword must contain at least 8 characters,one letter and one digit");
                alert.showAndWait();
            } else if (is_Valid_Email(emailField.getText()) == false) {
                validationField.setTextFill(Color.RED);
                validationField.setText("               Type a valid email");
            }
            else {
                try {
                    int ssn = Integer.parseInt(ssnField.getText());
                    String firstName = firstNameField.getText();
                    Date birthday = Date.valueOf(birthdateField.getValue());
                    String streetHouse = streetHouseField.getText();
                    String city = cityField.getValue();
                    String zipCode = zipCodeField.getText();
                    String position = positionField.getValue();
                    String password = passwordField.getText();
                    String confirmPassword = confirmPasswordField.getText();
                    String email = emailField.getText();
                    String lastName = lastNameField.getText();
                    String bankAcc = bankAccField.getText();
                    String gender = maleBtn.isSelected() ? "M" : femaleBtn.isSelected() ? "F" : "";
                    String phoneNumber = phoneNumberField.getText();
                    String department = departmentField.getValue();
                    int experience = Integer.parseInt(experienceField.getText());
                    int salary = Integer.parseInt(salaryField.getText());

                    Staff staff = new Staff(ssn, firstName, lastName, email, department, experience, birthday,
                            phoneNumber, position, salary, city + ", " + zipCode + ", " + streetHouse, gender,
                            Date.valueOf(java.time.LocalDate.now()), bankAcc, password, salt, isAdmin);

                    if(saveBtn.getText().equals("Insert")) {
                        if (HttpClient.insertStaffRequest(staff).equals("{message: Staff created!}")) {
                            validationField.setTextFill(Color.GREEN);
                            validationField.setText("Your account has been created successfully");
                            SessionManager.allStaff = (ArrayList<Staff>) HttpClient.getStaffRequest(SessionManager.user);
                        } else {
                            String errStr="";
                            ArrayList<Notification> errors=Authenticate.getNotifications();
                            for (int i = 0; i < errors.size(); i++) {
                                errStr+="* "+errors.get(i).getMsg();
                            }
                            Authenticate.clearNotificaitons();
                            validationField.setTextFill(Color.RED);
                            validationField.setText(errStr);
                        }
                    }
                    else if(saveBtn.getText().equals("Update")) {

                        if (HttpClient.updateStaffRequest(staff).equals("{message: Staff updated!}")) {
                            validationField.setTextFill(Color.GREEN);
                            validationField.setText("Your account has been updated successfully");
                            SessionManager.allStaff = (ArrayList<Staff>) HttpClient.getStaffRequest(SessionManager.user);
                        } else {
                            String errStr="";
                            ArrayList<Notification> errors=Authenticate.getNotifications();
                            for (int i = 0; i < errors.size(); i++) {
                                errStr+="* "+errors.get(i).getMsg();
                            }
                            Authenticate.clearNotificaitons();
                            validationField.setTextFill(Color.RED);
                            validationField.setText(errStr);
                        }
                    }
                    SessionManager.allStaff = (ArrayList<Staff>) HttpClient.getStaffRequest(SessionManager.user);
                    parentController.setView(MainScreenController.STAFF_LIST_VIEW);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Invalid credentials!");
            alert.showAndWait();
        }


    }

    public static boolean is_Valid_Password(String password) {

        if (password.length() < 8) return false;

        int charCount = 0;
        int numCount = 0;
        for (int i = 0; i < password.length(); i++) {

            char ch = password.charAt(i);

            if (is_Numeric(ch)) numCount++;
            else if (is_Letter(ch)) charCount++;
            else return false;
        }


        return (charCount >= 1 && numCount >= 1);
    }

    public static boolean is_Letter(char ch) {
        ch = Character.toUpperCase(ch);
        return (ch >= 'A' && ch <= 'Z');
    }


    public static boolean is_Numeric(char ch) {

        return (ch >= '0' && ch <= '9');
    }

    public static boolean is_Valid_Email(String email) {
        boolean rez;
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches() == true) {
            rez = true;
        } else
            rez = false;
        return rez;
    }
    public void setStaff(Staff s){
        ssnField.setText(Integer.toString(s.getSSN()));
        firstNameField.setText(s.getName());
        birthdateField.setValue(s.getBirthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        streetHouseField.setText(s.getAddress().split(",")[2]);
        cityField.setValue(s.getAddress().split(",")[0]);
        zipCodeField.setText(s.getAddress().split(",")[1]);
        positionField.setValue(s.getPosition());
        passwordField.setText(s.getPassword());
        confirmPasswordField.setText(s.getPassword());
        emailField.setText(s.getEmail());
        lastNameField.setText(s.getSurname());
        bankAccField.setText(s.getBankAccountNr());
        if (s.getGender().equals("M")) {
            maleBtn.setSelected(true);
        } else {
            femaleBtn.setSelected(true);
        }
        phoneNumberField.setText(s.getPhoneNumber());
        departmentField.setValue(s.getDepartment());
        experienceField.setText(Integer.toString(s.getExperience()));
        salaryField.setText(Integer.toString(s.getSalary()));
        salt = s.getSalt();
        isAdmin = s.isAdmin();
    }
    public void setBtnName(String btnName){
        saveBtn.setText(btnName);
    }
    public void disableSaveBtn(){
        this.saveBtn.setDisable(true);
    }
}