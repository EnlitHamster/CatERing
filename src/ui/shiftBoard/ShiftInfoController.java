package ui.shiftBoard;

import businesslogic.kitchen.KitchenJob;
import businesslogic.kitchen.KitchenJobsManager;
import businesslogic.shift.KitchenShift;
import businesslogic.shift.ShiftManager;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class ShiftInfoController {
    @FXML ListView<User> availableCooks;
    @FXML ListView<KitchenJob> assignedJobList;
    @FXML Label shiftInfoLabel;

    private Stage myStage;

    private ShiftListItem shiftItem;

    private void setLabel(){
        KitchenShift shift = shiftItem.getReferredShift();
        shiftInfoLabel.setText(shiftItem.toString() + " " + (shift.isComplete() ? "(Completo)" : ""));
    }

    public void init(Stage stage, ShiftListItem shiftItem){
        myStage = stage;
        this.shiftItem = shiftItem;
        KitchenShift shift = shiftItem.getReferredShift();
        setLabel();
        availableCooks.setItems(FXCollections.observableList(shift.getAvailableCooks()));
        assignedJobList.setItems(FXCollections.observableList(shift.getJobs()));
    }

    public void close() {
        myStage.close();
    }

    public void setComplete(ActionEvent actionEvent) {
        KitchenShift shift = shiftItem.getReferredShift();
        ShiftManager.getInstance().setKitchenShiftComplete(shift, !shift.isComplete());
        setLabel();
    }
}
