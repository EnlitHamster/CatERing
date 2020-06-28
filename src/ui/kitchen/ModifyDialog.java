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
            if (!quantity.getText().isEmpty() && !quantity.getText().isBlank()) {
                int p = Integer.parseInt(quantity.getText());
                if (p < 0) throw new IllegalArgumentException();
                workingJob.setQuantity(p);
            }

            if (!estimate.getText().isEmpty() && !estimate.getText().isBlank()) {
                long p = Long.parseLong(estimate.getText());
                if (p < 0) throw new IllegalArgumentException();
                workingJob.setTimeEstimate(p);
            }

            myStage.close();
        }
        catch (IllegalArgumentException e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Quantità e Stima tempo devono essere interi positivi");
            alert.showAndWait();
        }
    }

    public void cancelPressed() {
        myStage.close();
    }
}
