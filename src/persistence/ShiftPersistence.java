package persistence;

import businesslogic.kitchen.KitchenJob;
import businesslogic.shift.KitchenShift;
import businesslogic.shift.ShiftEventReceiver;

public class ShiftPersistence implements ShiftEventReceiver {
    @Override
    public void updateKitchenJobRemoved(KitchenJob job) {
        KitchenShift.deleteJobAssignment(job);
    }

    @Override
    public void updateKitchenJobAdded(KitchenJob job) {
        KitchenShift.saveJobAssigned(job);
    }
}
