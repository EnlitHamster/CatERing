package ui.shiftBoard;

import businesslogic.shift.KitchenShift;

class ShiftListItem {
    private KitchenShift referredShift;
    private String hour;

    ShiftListItem(String hour){
        this.hour = hour;
    }

    public void setRefferredShift(KitchenShift ks){
        referredShift = ks;
    }

    public KitchenShift getReferredShift() {
        return referredShift;
    }

    @Override
    public String toString() {
        return hour + " " + (referredShift == null ? "(Vuoto)" : "");
    }

    public boolean isEmpty(){return referredShift == null;}
}
