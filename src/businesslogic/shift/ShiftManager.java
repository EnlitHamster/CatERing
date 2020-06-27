package businesslogic.shift;

import businesslogic.kitchen.KitchenJob;
import businesslogic.kitchen.KitchenJobsEventReceiver;

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

    private List<KitchenShift> shifts;
    private final List<ShiftEventReceiver> eventReceivers;

    public ShiftManager(){
        shifts = new ArrayList<>();
        eventReceivers = new ArrayList<>();
    }

    private void notifyKitchenJobRemoved(KitchenJob job) {
        for (ShiftEventReceiver er : eventReceivers) {
            er.updateKitchenJobRemoved(job);
        }
    }

    private void notifyKitchenJobAdded(KitchenJob job) {
        for (ShiftEventReceiver er : eventReceivers) {
            er.updateKitchenJobAdded(job);
        }
    }

    public void removeKitchenJob(KitchenJob job){
        job.getShift().removeJob(job);
    }

    public void removeAllKitchenJob(Collection<KitchenJob> jobs){
        for (KitchenJob j:jobs) {
            removeKitchenJob(j);
        }
    }

    public List<KitchenShift> getShiftBoard(){
        return shifts;
    }

    public List<KitchenShift> getKitchenShiftBoard() {
        return shifts;
    }

    public void setKitchenShiftComplete(KitchenShift shift, boolean complete) {
        shift.setComplete(complete);
    }

    public void addKitchenJob(KitchenJob job, KitchenShift shift){
        shift.addJob(job);
    }

    public void addEventReceiver(ShiftEventReceiver rec) {
        this.eventReceivers.add(rec);
    }

    public void removeEventReceiver(ShiftEventReceiver rec) {
        this.eventReceivers.remove(rec);
    }
}
