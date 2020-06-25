package businesslogic.shift;

import businesslogic.kitchen.KitchenJob;
import businesslogic.user.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class KitchenShift extends Shift{
    private boolean isComplete;
    private List<KitchenJob> jobs;
    private List<User> availableCooks;

    public KitchenShift(Calendar date){
        super(date);
        jobs = new ArrayList<KitchenJob>();
        availableCooks = new ArrayList<User>();
    }

    public boolean isAvailable(User chef){
        boolean available = false;
        if(chef.isChef()){
             available = availableCooks.contains(chef);
        }
        return available;
    }

    public void addJob(KitchenJob job){jobs.add(job);}
    public void removeJob(KitchenJob job){jobs.remove(job);}
}
