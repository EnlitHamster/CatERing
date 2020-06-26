package businesslogic.shift;

import businesslogic.kitchen.KitchenJob;
import javafx.collections.transformation.SortedList;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<KitchenShift> getKitchenShiftBoard() {
        return shifts.stream()
                .filter(KitchenShift.class::isInstance)
                .map(KitchenShift.class::cast)
                .collect(Collectors.toList());
    }

    public void setKitchenShiftComplete(KitchenShift shift, boolean complete) {
        shift.setComplete(complete);
    }

    public void addKitchenJob(KitchenJob job, KitchenShift shift){
        shift.addJob(job);
    }
}
