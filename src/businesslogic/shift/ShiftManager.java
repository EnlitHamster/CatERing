package businesslogic.shift;

import businesslogic.kitchen.KitchenJob;
import javafx.collections.transformation.SortedList;

import java.util.*;

public class ShiftManager {
    private static ShiftManager instance;

    public static ShiftManager getInstance(){
        if(instance == null){
            instance = new ShiftManager();
        }
        return instance;
    }

    private List<Shift> shifts;

    public ShiftManager(){
        shifts = new ArrayList<>();
    }

    private void notifyKitchenJobRemoved(KitchenJob job){
        //to implement
    }

    private void notifyKitchenJobAdded(KitchenJob job){
        //to implement
    }

    public void removeKitchenJob(KitchenJob job){
        job.getShift().removeJob(job);
    }

    public void removeAllKitchenJob(Collection<KitchenJob> jobs){
        for (KitchenJob j:jobs) {
            removeKitchenJob(j);
        }
    }

    public List<Shift> getShiftBoard(){
        return shifts;
    }

    public void addKitchenJobToShift(KitchenJob job, KitchenShift shift){
        shift.addJob(job);
    }

    public void removeKitchenJobFromShift(KitchenJob job, KitchenShift shift){
        shift.removeJob(job);
    }






}
