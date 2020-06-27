package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.kitchen.KitchenJob;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifyDialog {
    @FXML TextField quantity;
    @FXML TextField estimate;
    Stage myStage;
    KitchenJob workingJob;

    public void init(Stage stage, KitchenJob selectedJob){
        myStage = stage;
        workingJob = selectedJob;
    }

    public void okPressed() {
        try {
            workingJob.setQuantity(Integer.parseInt(quantity.getText()));
            workingJob.setTimeEstimate(Long.parseLong(estimate.getText()));
            myStage.close();
        }
        catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "La quantita' dev'essere un intero, mentre la stima di tempo un decimale");
            alert.showAndWait();
        }
    }

    public void cancelPressed() {
        myStage.close();
    }
}
