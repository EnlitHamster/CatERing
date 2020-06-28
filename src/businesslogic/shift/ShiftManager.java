package businesslogic.shift;

import businesslogic.kitchen.KitchenJob;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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

    public static final String[] month = {
            "Gen",
            "Feb",
            "Mar",
            "Apr",
            "Mag",
            "Giu",
            "Lug",
            "Ago",
            "Set",
            "Ott",
            "Nov",
            "Dic"
    };

    public static int monthToInt(String month){
        switch (month) {
            case "Gen":
                return Calendar.JANUARY;
            case "Feb":
                return Calendar.FEBRUARY;
            case "Mar":
                return Calendar.MARCH;
            case "Apr":
                return Calendar.APRIL;
            case "Mag":
                return Calendar.MAY;
            case "Giu":
                return Calendar.JUNE;
            case "Lug":
                return Calendar.JULY;
            case "Ago":
                return Calendar.AUGUST;
            case "Set":
                return Calendar.SEPTEMBER;
            case "Ott":
                return Calendar.OCTOBER;
            case "Nov":
                return Calendar.NOVEMBER;
            case "Dic":
                return Calendar.DECEMBER;
            default:
                return -1;
        }
    }

    private final List<KitchenShift> shifts;
    private final List<ShiftEventReceiver> eventReceivers;

    private ShiftManager(){
        shifts = KitchenShift.loadAllKitchenShifts();
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

    private void notifyKitchenShiftAdded(KitchenShift shift) {
        for (ShiftEventReceiver er : eventReceivers) {
            er.updateKitchenShiftAdded(shift);
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

    public KitchenShift getKitchenShift(Calendar date) {
        for (KitchenShift shift : shifts) {
            Calendar shiftDate = Calendar.getInstance();
            shiftDate.setTimeInMillis(shift.getDate().getTime());
            if (shiftDate.get(Calendar.HOUR_OF_DAY) == date.get(Calendar.HOUR_OF_DAY) &&
                    shiftDate.get(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH) &&
                    shiftDate.get(Calendar.MONTH) == date.get(Calendar.MONTH))
                return shift;
        }
        return null;
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

    public void addKitchenJob(KitchenJob job){
        job.getShift().addJob(job);
        this.notifyKitchenJobAdded(job);
    }

    public KitchenShift createKitchenShift(Timestamp date) {
        KitchenShift shift = new KitchenShift(date);
        shifts.add(shift);
        this.notifyKitchenShiftAdded(shift);
        return shift;
    }

    public void addEventReceiver(ShiftEventReceiver rec) {
        this.eventReceivers.add(rec);
    }

    public void removeEventReceiver(ShiftEventReceiver rec) {
        this.eventReceivers.remove(rec);
    }
}
