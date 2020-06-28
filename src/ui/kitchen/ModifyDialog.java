package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.kitchen.KitchenJob;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
            Integer pQuantity = null;
            if (!quantity.getText().isEmpty() && !quantity.getText().isBlank()) {
                pQuantity = Integer.parseInt(quantity.getText());
                if (pQuantity < 0) throw new IllegalArgumentException();
            }

            Long pEstimate = null;
            if (!estimate.getText().isEmpty() && !estimate.getText().isBlank()) {
                pEstimate = Long.parseLong(estimate.getText());
                if (pEstimate < 0) throw new IllegalArgumentException();
            }

            CatERing.getInstance().getKitchenManager().assignJobInfo(workingJob, pEstimate, pQuantity);
            myStage.close();
        }
        catch (IllegalArgumentException e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Quantità e Stima tempo devono essere interi positivi");
            alert.showAndWait();
            e.printStackTrace();
        } catch (UseCaseLogicException e) {
            e.printStackTrace();
        }
    }

    public void cancelPressed() {
        myStage.close();
    }
}
