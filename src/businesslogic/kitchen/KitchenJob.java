package businesslogic.kitchen;

import businesslogic.recipe.KitchenTask;
import businesslogic.shift.KitchenShift;
import businesslogic.user.User;

public class KitchenJob {
    private Long timeEstimate;
    private Integer quantity;
    private boolean isComplete;
    private User assignedCook;
    private KitchenShift shift;
    private final KitchenTask itemTask;

    public KitchenJob(KitchenTask kt) {
        itemTask = kt;
        isComplete = false;
        assignedCook = null;
        shift = null;
    }

    public Long getTimeEstimate() {
        return timeEstimate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public User getAssignedCook() {
        return assignedCook;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public KitchenShift getShift() {
        return shift;
    }

    public void setTimeEstimate(Long timeEstimate) {
        if (timeEstimate <= 0) throw new IllegalArgumentException();
        this.timeEstimate = timeEstimate;
    }

    public void setQuantity(Integer quantity) {
        if (quantity < 0) throw new IllegalArgumentException();
        this.quantity = quantity;
    }

    public void setComplete() {
        this.isComplete = true;
    }

    public void setAssignedCook(User u) {
        this.assignedCook = u;
    }

    public void setShift(KitchenShift ks) {
        this.shift = ks;
    }

    public boolean preparesTask(KitchenTask task) {
        return itemTask.equals(task);
    }

    public boolean hasShift() {
        return shift != null;
    }
}
