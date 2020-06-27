package businesslogic.recipe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;

import java.util.*;

public abstract class KitchenTask {
    private static final Map<Integer, KitchenTask> all = new HashMap<>();

    protected Integer id;
    protected String name;
    protected String notes;
    protected List<String> tags;
    protected List<Preparation> usedPreparations;

    protected KitchenTask() {
        usedPreparations = new ArrayList<>();
    }

    public KitchenTask(String name) {
        id = 0;
        this.name = name;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof KitchenTask) return ((KitchenTask) obj).id.equals(id);
        else if (obj instanceof Integer) return obj.equals(id);
        else return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // STATIC METHODS FOR PERSISTENCE

    public static ObservableList<KitchenTask> loadAllTasks() {
        String query = "SELECT * FROM KitchenTasks";
        PersistenceManager.executeQuery(query, rs -> {
            int id = rs.getInt("id");
            if (all.containsKey(id)) {
                KitchenTask task = all.get(id);
                task.name = rs.getString("name");
            } else {
                KitchenTask task;
                if (rs.getBoolean("is recipe")) task = Recipe.loadRecipe(rs);
                else task = Preparation.loadPreparation(rs);
                task.id = id;
                all.put(task.id, task);
            }
        });
        query = "SELECT * FROM UsedPreparations";
        PersistenceManager.executeQuery(query, rs -> {
            int id = rs.getInt("recipe");
            int pid = rs.getInt("preparation");
            if (all.containsKey(id) && all.containsKey(pid)) {
                all.get(id).usedPreparations.add((Preparation) all.get(pid));
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
        var obj = new Object() {
            KitchenTask task;
            final List<Integer> preps = new ArrayList<>();
        };
        String query = "SELECT * FROM KitchenTasks WHERE id = " + id;
        PersistenceManager.executeQuery(query, rs -> {
                if (rs.getBoolean("is recipe")) obj.task = Recipe.loadRecipe(rs);
                else obj.task = Preparation.loadPreparation(rs);
                obj.task.id = id;
        });
        query = "SELECT preparation FROM UsedPreparations WHERE recipe = " + id;
        PersistenceManager.executeQuery(query, rs -> obj.preps.add(rs.getInt("preparation")));
        for (Integer pid : obj.preps) obj.task.usedPreparations.add((Preparation) loadTaskById(pid));
        all.put(obj.task.id, obj.task);
        return obj.task;
    }
}
