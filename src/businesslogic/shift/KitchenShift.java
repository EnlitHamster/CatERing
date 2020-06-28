package businesslogic.shift;

import businesslogic.kitchen.KitchenJob;
import businesslogic.user.User;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitchenShift implements Comparable<KitchenShift> {
    private static final Map<Integer, KitchenShift> loadedShifts = new HashMap<>();

    private Integer id;
    private Timestamp date;
    private boolean isComplete;
    private final List<KitchenJob> jobs;
    private final List<User> availableCooks;

    @Override
    public String toString() {
        return date.toString();
    }

    private KitchenShift() {
        jobs = new ArrayList<>();
        availableCooks = new ArrayList<>();
    }

    public KitchenShift(Timestamp date){
        id = 0;
        this.date = date;
        jobs = new ArrayList<>();
        availableCooks = new ArrayList<>();
    }

    public Timestamp getDate(){return date;}

    @Override
    public int compareTo(KitchenShift o) {
        return date.compareTo(o.date);
    }

    public boolean isAvailable(User cook){
        boolean available = false;
        if(cook.isCook()){
             available = availableCooks.contains(cook);
        }
        return available;
    }

    public void addJob(KitchenJob job){jobs.add(job);}
    public void removeJob(KitchenJob job){jobs.remove(job);}

    public void setComplete(boolean b) {
        isComplete = b;
    }

    public List<User> getAvailableCooks(){return availableCooks;}
    public List<KitchenJob> getJobs(){return jobs;}

    // STATIC METHODS FOR PERSISTENCE

    public static void saveJobAssigned(KitchenJob j){
        String query =
                "UPDATE kitchenjobs " +
                "SET shift = " + j.getShift().id + " " +
                "WHERE id = " + j.getId();
        PersistenceManager.executeUpdate(query);

        query = "SELECT * FROM kitchenshifts WHERE id = " + j.getShift().id;
        var obj = new Object() {boolean exists = false;};
        PersistenceManager.executeQuery(query, rs -> obj.exists = true);

        if (!obj.exists) {
            String upd = "INSERT INTO kitchenshifts (id, date, `is complete`) " +
                    "VALUES (" + j.getShift().id + ", " + j.getShift().date.getTime() + ", " + false + ");";
            PersistenceManager.executeUpdate(upd);
        }
    }

    public static void deleteJobAssignment(KitchenJob j){
        String query =
                "UPDATE kitchenjobs " +
                "SET shift = NULL " +
                "WHERE id = " + j.getId();
        PersistenceManager.executeUpdate(query);
    }

    public static void saveNewKitchenShift(KitchenShift shift) {
        String ins = "INSERT INTO kitchenshifts (id, date, `is complete`) VALUES(?, ?, ?);";
        PersistenceManager.executeBatchUpdate(ins, 1, new BatchUpdateHandler() {
            @Override
            public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
                ps.setInt(1, shift.id);
                ps.setTimestamp(2, shift.date);
                ps.setBoolean(3, shift.isComplete);
            }

            @Override
            public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
                // should be only one
                if (count == 0) {
                    shift.id = rs.getInt(1);
                }
            }
        });
        loadedShifts.put(shift.id, shift);
    }

    public static List<KitchenShift> loadAllKitchenShifts() {
        String query = "SELECT * FROM kitchenshifts";
        List<KitchenShift> shifts = new ArrayList<>();
        PersistenceManager.executeQuery(query, rs -> {
            Integer id = rs.getInt("id");
            if (!loadedShifts.containsKey(id)) {
                KitchenShift shift = new KitchenShift();
                shift.id = id;
                shift.date = rs.getTimestamp("date");
                shift.isComplete = rs.getBoolean("is complete");
                shifts.add(shift);
                loadedShifts.put(id, shift);
            }
        });
        for (KitchenShift shift : shifts) {
            query = "SELECT cook FROM shiftavailablecooks WHERE shift = " + shift.id;
            List<Integer> ids = new ArrayList<>();
            PersistenceManager.executeQuery(query, rs -> ids.add(rs.getInt("cook")));
            for (Integer uid : ids) shift.availableCooks.add(User.loadUserById(uid));
        }
        for (KitchenShift shift : shifts) {
            query = "SELECT id FROM kitchenjobs WHERE shift = " + shift.id;
            List<Integer> ids = new ArrayList<>();
            PersistenceManager.executeQuery(query, rs -> ids.add(rs.getInt("id")));
            for (Integer jid : ids) shift.jobs.add(KitchenJob.loadKitchenJobFromShift(shift, jid));
        }
        return new ArrayList<>(loadedShifts.values());
    }

    public static KitchenShift loadKitchenShiftFromJob(Integer jobId, Integer id) {
        if (id == 0) return null;
        if (loadedShifts.containsKey(id)) return loadedShifts.get(id);
        String query = "SELECT * FROM kitchenshifts WHERE id = " + id;
        KitchenShift shift = new KitchenShift();
        PersistenceManager.executeQuery(query, rs -> {
            shift.date = rs.getTimestamp("date");
            shift.isComplete = rs.getBoolean("is complete");
        });
        query = "SELECT cook FROM shiftavailablecooks WHERE shift = " + id;
        List<Integer> ids = new ArrayList<>();
        PersistenceManager.executeQuery(query, rs -> ids.add(rs.getInt("cook")));
        for (Integer uid : ids) shift.availableCooks.add(User.loadUserById(uid));
        query = "SELECT id FROM kitchenjobs WHERE shift = " + id;
        ids.clear();
        PersistenceManager.executeQuery(query, rs -> ids.add(rs.getInt("id")));
        ids.remove(jobId);
        for (Integer jid : ids) shift.jobs.add(KitchenJob.loadKitchenJobFromShift(shift, jid));
        return shift;
    }

    public static void saveShiftComplete(KitchenShift shift) {
        String upd = "UPDATE kitchenshifts SET `is complete` = " + shift.isComplete + " WHERE id = " + shift.id;
        PersistenceManager.executeUpdate(upd);
    }

    public boolean isComplete() {
        return isComplete;
    }
}
