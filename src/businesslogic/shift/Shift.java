package businesslogic.shift;

import businesslogic.user.User;

import java.util.Calendar;

public abstract class Shift {
    Calendar date;
    abstract boolean isAvailable(User u);

    public Shift(Calendar date){
        this.date = date;
    }

    public Calendar getDate(){return date;}
}
