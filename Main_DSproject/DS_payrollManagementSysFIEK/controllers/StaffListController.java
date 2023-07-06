package controllers;

import Utils.SessionManager;
import client.HttpClient;
import components.ErrorPopupComponent;
import components.PaginationComponent;
import components.StaffCardComponent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Staff;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StaffListController extends ChildController{
    private final int PAGE_SIZE = 6;

    private PaginationComponent paginationComponent;

    @FXML
    private VBox staffPane;
    @FXML
    private HBox btnPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paginationComponent = new PaginationComponent(SessionManager.allStaff.size(), PAGE_SIZE);
        try {
            showStaff(0);
        } catch (Exception e) {
            ErrorPopupComponent.show(e);
        }
        paginationComponent.render(btnPane,page -> {
            try {
                showStaff(page);
            } catch (Exception ex) {
                ErrorPopupComponent.show(ex);
            }
        });
    }

    private void showStaff(int page) throws Exception {
        List<Staff> staff = getStaff(PAGE_SIZE, page);
        staffPane.getChildren().clear();
        for (Staff s  : staff) {
            Node staffCard = new StaffCardComponent().getContent(s , e->viewStaffDetails(s), e->removeStaff(s), e->editStaff(s));
            staffPane.getChildren().add(staffCard);
        }

    }


    private void viewStaffDetails(Staff s){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../Views/staffDetails.fxml"));

            Pane pane = loader.load();
            StaffDetailsController controller = loader.getController();
            controller.setStaff(s);
            controller.disableSaveBtn();
            parentController.setView(MainScreenController.STAFF_DETAILS_VIEW, pane, controller);
        } catch (Exception e) {
            ErrorPopupComponent.show(e);
        }
    }
    private void removeStaff(Staff s) {
        try {
            if(SessionManager.user.getSSN() == s.getSSN()) {
                HttpClient.deleteStaffRequest(s);
                System.exit(0);
            }
            HttpClient.deleteStaffRequest(s);
            SessionManager.allStaff = (ArrayList<Staff>) HttpClient.getStaffRequest(SessionManager.user);
            showStaff(paginationComponent.getCursor());
        } catch (Exception e) {
            ErrorPopupComponent.show(e);
        }
    }
    private void editStaff(Staff s) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../Views/staffDetails.fxml"));

            Pane pane = loader.load();
            StaffDetailsController controller = loader.getController();
            controller.setStaff(s);
            controller.setBtnName("Update");
            parentController.setView(MainScreenController.STAFF_DETAILS_VIEW, pane, controller);
        } catch (Exception e) {
            ErrorPopupComponent.show(e);
        }
    }

    private List<Staff> getStaff(int pageSize, int page){
        ArrayList<Staff> someStaff = new ArrayList<>();
        for (int i= pageSize * page ; i< pageSize * page + pageSize && i < SessionManager.allStaff.size();i++){
                someStaff.add(SessionManager.allStaff.get(i));
        }
        return someStaff;
    }
}
