package ui.shiftBoard;

import businesslogic.kitchen.KitchenJob;
import businesslogic.shift.KitchenShift;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class ShiftInfoController {
    @FXML ListView<User> availableCooks;
    @FXML ListView<KitchenJob> assignedJobList;
    @FXML Label shiftInfoLabel;

    private Stage myStage;

    public void init(Stage stage, ShiftListItem shiftItem){
        myStage = stage;
        shiftInfoLabel.setText(shiftItem.toString());
        KitchenShift shift = shiftItem.getReferredShift();
        availableCooks.setItems(FXCollections.observableList(shift.getAvailableCooks()));
        assignedJobList.setItems(FXCollections.observableList(shift.getJobs()));
    }

    public void close() {
        myStage.close();
    }


}
