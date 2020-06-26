package businesslogic.kitchen;

import businesslogic.recipe.KitchenTask;
import businesslogic.shift.KitchenShift;
import businesslogic.user.User;
import persistence.PersistenceManager;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class KitchenJob {
    private static final Map<Integer, KitchenJob> loadedJobs = new HashMap<>();

    private Integer id; // TODO: add persistence method for save with id update (see Menu)
    private Long timeEstimate;
    private Integer quantity;
    private boolean isComplete;
    private User assignedCook;
    private KitchenShift shift;
    private KitchenTask itemTask;

    private KitchenJob() {}

    public KitchenJob(KitchenTask kt) {
        itemTask = kt;
        isComplete = false;
        assignedCook = null;
        shift = null;
    }

    public Integer getId() {return id;}

    public Long getTimeEstimate() {
        return timeEstimate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public User getAssignedCook() {
        return assignedCook;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public KitchenShift getShift() {
        return shift;
    }

    public void setTimeEstimate(Long timeEstimate) {
        if (timeEstimate <= 0) throw new IllegalArgumentException();
        this.timeEstimate = timeEstimate;
    }

    public void setQuantity(Integer quantity) {
        if (quantity < 0) throw new IllegalArgumentException();
        this.quantity = quantity;
    }

    public void setComplete() {
        this.isComplete = true;
    }

    public void setAssignedCook(User u) {
        this.assignedCook = u;
    }

    public void setShift(KitchenShift ks) {
        this.shift = ks;
    }

    public boolean preparesTask(KitchenTask task) {
        return itemTask.equals(task);
    }

    public boolean hasShift() {
        return shift != null;
    }

    // STATIC METHODS FOR PERSISTENCE

    public static KitchenJob loadKitchenJobFromShift(KitchenShift shift, Integer id) {
        if (loadedJobs.containsKey(id)) return loadedJobs.get(id);
        String query = "SELECT * FROM KitchenJobs WHERE id = " + id;
        var obj = new Object() {Integer tid, uid;};
        KitchenJob job = new KitchenJob();
        job.id = id;
        PersistenceManager.executeQuery(query, rs -> {
            job.timeEstimate = rs.getLong("time estimate");
            job.quantity = rs.getInt("quantity");
            job.isComplete = rs.getBoolean("is complete");
            obj.uid = rs.getInt("assigned cook");
            obj.tid = rs.getInt("item task");
        });
        job.assignedCook = User.loadUserById(obj.uid);
        job.shift = shift;
        job.itemTask = KitchenTask.loadTaskById(obj.tid);
        loadedJobs.put(job.id, job);
        return job;
    }

    public static KitchenJob loadKitchenJobById(Integer id) {
        if (loadedJobs.containsKey(id)) return loadedJobs.get(id);
        String query = "SELECT * FROM KitchenJobs WHERE id = " + id;
        var obj = new Object() {Integer tid, uid; Timestamp date;};
        KitchenJob job = new KitchenJob();
        job.id = id;
        PersistenceManager.executeQuery(query, rs -> {
            job.timeEstimate = rs.getLong("time estimate");
            job.quantity = rs.getInt("quantity");
            job.isComplete = rs.getBoolean("is complete");
            obj.uid = rs.getInt("assigned cook");
            obj.date = rs.getTimestamp("shift");
            obj.tid = rs.getInt("item task");
        });
        job.assignedCook = User.loadUserById(obj.uid);
        job.shift = KitchenShift.loadKitchenShiftFromJob(id, obj.date);
        job.itemTask = KitchenTask.loadTaskById(obj.tid);
        loadedJobs.put(job.id, job);
        return job;
    }
}
