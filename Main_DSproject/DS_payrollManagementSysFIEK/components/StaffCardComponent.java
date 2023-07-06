package components;

import controllers.partials.StaffCardController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import models.Staff;

public class StaffCardComponent {
    public Node getContent(Staff s, EventHandler<ActionEvent> viewDetails, EventHandler<ActionEvent> deleteHandler, EventHandler<ActionEvent> editHandler) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../Views/partials/staffCard.fxml"));
        Node node = loader.load();

        StaffCardController controller = loader.getController();
        controller.setStaff(s);

        controller.setOnViewDetailsAction(viewDetails);
        controller.setOnDeleteAction(deleteHandler);
        controller.setOnEditAction(editHandler);

        return node;
    }
}
