package businesslogic.shift;

import businesslogic.kitchen.KitchenJob;
import businesslogic.user.User;
import persistence.PersistenceManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class KitchenShift implements Comparable<KitchenShift> {
    protected Timestamp date;
    private boolean isComplete;
    private final List<KitchenJob> jobs;
    private final List<User> availableCooks;

    private KitchenShift() {
        jobs = new ArrayList<>();
        availableCooks = new ArrayList<>();
    }

    public KitchenShift(Timestamp date){
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
                "SET shift = " + j.getShift().getDate() +
                "WHERE id = " + j.getId();
        PersistenceManager.executeUpdate(query);

        query = "SELECT * FROM kitchenshifts WHERE id = " + j.getShift().getDate();
        var obj = new Object() {boolean exists = false;};
        PersistenceManager.executeQuery(query, rs -> {
            obj.exists = true;
        });

        if (!obj.exists) {
            String upd = "INSERT INTO kitchenshifts (date, `is complete`) VALUES ("+j.getShift().getDate()+", "+false+");";
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

    public static KitchenShift loadKitchenShiftFromJob(Integer jobId, Timestamp id) {
        String query = "SELECT * FROM kitchenshifts WHERE date = " + id;
        KitchenShift shift = new KitchenShift();
        PersistenceManager.executeQuery(query, rs -> {
            shift.date = id;
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
        String upd = "UPDATE kitchenshifts SER `is complete` = " + shift.isComplete + " WHERE date = " + shift.date;
        PersistenceManager.executeUpdate(upd);
    }
}
