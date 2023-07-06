package controllers;

import javafx.fxml.Initializable;

public abstract class ChildController implements Initializable {
    public MainScreenController parentController;

    public void setParentController(MainScreenController parentController) {
        this.parentController = parentController;
    }
    
}
