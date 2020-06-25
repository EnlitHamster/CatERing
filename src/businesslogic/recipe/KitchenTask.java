package businesslogic.recipe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;

import java.util.*;

public class KitchenTask {
    private static final Map<Integer, KitchenTask> all = new HashMap<>();

    private int id;
    private String name;
    private List<String> notes;
    private List<String> tags;
    private List<Preparation> usedPreparations;

    private KitchenTask() {}

    public KitchenTask(String name) {
        id = 0;
        this.name = name;
        notes = new ArrayList<>();
        tags = new ArrayList<>();
        usedPreparations = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return name;
    }

    public List<Preparation> getAllUsedPreparations() {
        List<Preparation> allPreparations = new ArrayList<>(usedPreparations);
        for (Preparation p : usedPreparations) allPreparations.addAll(p.getAllUsedPreparations());
        return allPreparations;
    }

    // STATIC METHODS FOR PERSISTENCE

    public static ObservableList<KitchenTask> loadAllTasks() {
        String query = "SELECT * FROM Recipes";
        PersistenceManager.executeQuery(query, rs -> {
            int id = rs.getInt("id");
            if (all.containsKey(id)) {
                KitchenTask task = all.get(id);
                task.name = rs.getString("name");
            } else {
                KitchenTask task = new KitchenTask(rs.getString("name"));
                task.id = id;
                all.put(task.id, task);
            }
        });
        ObservableList<KitchenTask> ret =  FXCollections.observableArrayList(all.values());
        ret.sort(Comparator.comparing(KitchenTask::getName));
        return ret;
    }

    public static ObservableList<KitchenTask> getAllTasks() {
        return FXCollections.observableArrayList(all.values());
    }

    public static KitchenTask loadTaskById(int id) {
        if (all.containsKey(id)) return all.get(id);
        KitchenTask task = new KitchenTask();
        String query = "SELECT * FROM Recipes WHERE id = " + id;
        PersistenceManager.executeQuery(query, rs -> {
                task.name = rs.getString("name");
                task.id = id;
                all.put(task.id, task);
        });
        return task;
    }


}
