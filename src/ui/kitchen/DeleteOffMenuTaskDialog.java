package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.kitchen.KitchenJobsException;
import businesslogic.recipe.KitchenTask;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class DeleteOffMenuTaskDialog {
    @FXML
    ComboBox<KitchenTask> taskComboBox;
    @FXML
    Button deleteButton;

    private Stage myStage;

    public void init(Stage stage){
        taskComboBox.setItems(CatERing.getInstance().getKitchenManager().getOffMenuKitchenTask());
        myStage = stage;
    }

    public void deleteButtonPressed() {
        KitchenTask selectedTask = taskComboBox.getValue();
        if(selectedTask != null){
            try {
                CatERing.getInstance().getKitchenManager().deleteOffMenuRecipe(selectedTask);
                myStage.close();
            } catch (UseCaseLogicException | KitchenJobsException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancelButtonPressed() {
        myStage.close();
    }

    public void comboBoxChanged() {
        deleteButton.setDisable(taskComboBox.getValue() == null);
    }
}
