package businesslogic.recipe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class KitchenTaskManager {

    public KitchenTaskManager() {
        KitchenTask.loadAllTasks();
    }

    public ObservableList<KitchenTask> getKitchenTasks() {
        return FXCollections.unmodifiableObservableList(KitchenTask.getAllTasks());
    }
}
