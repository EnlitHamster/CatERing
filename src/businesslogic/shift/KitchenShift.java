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
                "UPDATE KitchenJobs " +
                "SET shift = " + j.getShift() +
                "WHERE id = " + j.getId();
        PersistenceManager.executeUpdate(query);
    }

    public static void deleteJobAssignment(KitchenJob j){
        String query =
                "UPDATE KitchenJobs " +
                "SET shift = NULL " +
                "WHERE id = " + j.getId();
        PersistenceManager.executeUpdate(query);
    }

    public static KitchenShift loadKitchenShiftFromJob(Integer jobId, Timestamp id) {
        String query = "SELECT * FROM KitchenShifts WHERE date = " + id;
        KitchenShift shift = new KitchenShift();
        PersistenceManager.executeQuery(query, rs -> {
            shift.date = id;
            shift.isComplete = rs.getBoolean("is complete");
        });
        query = "SELECT cook FROM ShiftAvailableCooks WHERE shift = " + id;
        List<Integer> ids = new ArrayList<>();
        PersistenceManager.executeQuery(query, rs -> ids.add(rs.getInt("cook")));
        for (Integer uid : ids) shift.availableCooks.add(User.loadUserById(uid));
        query = "SELECT id FROM KitchenJobs WHERE shift = " + id;
        ids.clear();
        PersistenceManager.executeQuery(query, rs -> ids.add(rs.getInt("id")));
        ids.remove(jobId);
        for (Integer jid : ids) shift.jobs.add(KitchenJob.loadKitchenJobFromShift(shift, jid));
        return shift;
    }
}
