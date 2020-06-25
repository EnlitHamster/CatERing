package businesslogic.shift;

import businesslogic.kitchen.KitchenJob;

public interface ShiftEventReceiver {
    void updateKitchenJobRemoved(KitchenJob job);
    void updateKitchenJobAdded(KitchenJob job);
}
