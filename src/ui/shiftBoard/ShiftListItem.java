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
        String str = hour + " ";
        if (referredShift == null) str += "(Vuoto)";
        else {
            str += "(" + referredShift.getJobs().size() + " Compiti | ";
            str += referredShift.getAvailableCooks().size() + " Cuochi disponibili)";
        }
        return str;
    }

    public boolean isEmpty(){return referredShift == null;}
}
