package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.recipe.KitchenTask;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class AddTaskDialog {

    @FXML ComboBox<KitchenTask> taskComboBox;
    @FXML Button insertButton;

    private Stage myStage;

    public void init(Stage stage){
        taskComboBox.setItems(CatERing.getInstance().getKitchenTaskManager().getKitchenTasks());
        myStage = stage;
    }

    public void insertButtonPressed() {
        KitchenTask selectedTask = taskComboBox.getValue();
        if(selectedTask != null){
            try {
                CatERing.getInstance().getKitchenManager().addTask(selectedTask);
                myStage.close();
            } catch (UseCaseLogicException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancelButtonPressed() {
        myStage.close();
    }

    public void comboBoxChanged() {
        insertButton.setDisable(taskComboBox.getValue() == null);
    }
}
