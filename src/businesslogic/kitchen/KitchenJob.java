package businesslogic.kitchen;

import businesslogic.recipe.KitchenTask;
import businesslogic.shift.KitchenShift;
import businesslogic.user.User;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitchenJob {
    private static final Map<Integer, KitchenJob> loadedJobs = new HashMap<>();

    private Integer id;
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

    public String toString(){
        return itemTask.getName();
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

    public KitchenTask getItemTask() {return itemTask;}

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

    public static void deleteKitchenJob(KitchenJob job) {
        String del = "DELETE FROM KitchenJobs WHERE id = " + job.id;
        PersistenceManager.executeUpdate(del);
        loadedJobs.remove(job.id);
    }

    public static void saveAllNewKitchenJobs(Integer serviceId, List<KitchenJob> jobs) {
        String menuInsert = "INSERT INTO KitchenJobs (item task, service, position) VALUES (?, ?, ?);";
        PersistenceManager.executeBatchUpdate(menuInsert, jobs.size(), new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                KitchenJob job = jobs.get(batchCount);
                ps.setInt(1, job.itemTask.getId());
                ps.setInt(2, serviceId);
                ps.setInt(3, batchCount);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // should be only one
                if (count == 0) {
                    KitchenJob job = jobs.get(count);
                    job.id = rs.getInt(1);
                    loadedJobs.put(job.id, job);
                }
            }
        });
    }

    public static void saveNewKitchenJob(Integer serviceId, KitchenJob job, Integer position) {
        String menuInsert = "INSERT INTO KitchenJobs (item task, service, position) VALUES (?, ?, ?);";
        PersistenceManager.executeBatchUpdate(menuInsert, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, job.itemTask.getId());
                ps.setInt(2, serviceId);
                ps.setInt(3, position);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // should be only one
                if (count == 0) {
                    job.id = rs.getInt(1);
                }
            }
        });
        loadedJobs.put(job.id, job);
    }

    public static void saveAssignment(KitchenJob job) {
        String upd = "UPDATE KitchenJobs SET cook = " + (job.assignedCook == null ? "NULL" : "'" + job.assignedCook.getId() + "'") +
                " WHERE id = " + job.id;
        PersistenceManager.executeUpdate(upd);
    }

    public static void saveUnassignment(KitchenJob job) {
        String upd = "UPDATE KitchenJobs SET cook = NULL" +
                " WHERE id = " + job.id;
        PersistenceManager.executeUpdate(upd);
    }

    public static void saveInfoAssignment(KitchenJob job) {
        String upd = "UPDATE KitchenJobs SET time estimate = '" + job.timeEstimate + "', quantity = '" + job.quantity + "'" +
                " WHERE id = " + job.id;
        PersistenceManager.executeUpdate(upd);
    }

    public static void saveJobCompleted(KitchenJob job) {
        String upd = "UPDATE KitchenJobs SET is complete = " + true + " WHERE id = " + job.id;
        PersistenceManager.executeUpdate(upd);
    }

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

    public boolean hasCook() {
        return assignedCook != null;
    }

    public String getAssignedCookName() {
        if(assignedCook == null) return "Non assegnato";
        else return assignedCook.getUserName();
    }

    public String getShiftString() {
        if(shift == null) return "Non assegnato";
        else return shift.toString();
    }


    public String getEstimateTime() {
        if(timeEstimate == null) return "0";
        else return String.valueOf(timeEstimate);
    }

    public String getQuantityString() {
        if(quantity == null) return "0";
        else return String.valueOf(quantity);
    }
}
