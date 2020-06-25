package businesslogic.recipe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RecipeManager {

    public RecipeManager() {
        KitchenTask.loadAllTasks();
    }

    public ObservableList<KitchenTask> getRecipes() {
        return FXCollections.unmodifiableObservableList(KitchenTask.getAllTasks());
    }
}
