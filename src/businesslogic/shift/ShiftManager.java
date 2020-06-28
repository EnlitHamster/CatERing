package businesslogic.shift;

import businesslogic.kitchen.KitchenJob;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    private void notifyKitchenShiftComplete(KitchenShift shift) {
        for (ShiftEventReceiver er : eventReceivers) {
            er.updateKitchenShiftComplete(shift);
        }
    }

    public void removeKitchenJob(KitchenJob job){
        job.getShift().removeJob(job);
        this.notifyKitchenJobRemoved(job);
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
        this.notifyKitchenShiftComplete(shift);
    }

    public void addKitchenJob(KitchenJob job, KitchenShift shift){
        shift.addJob(job);
        this.notifyKitchenJobAdded(job);
    }

    public void addEventReceiver(ShiftEventReceiver rec) {
        this.eventReceivers.add(rec);
    }

    public void removeEventReceiver(ShiftEventReceiver rec) {
        this.eventReceivers.remove(rec);
    }
}
