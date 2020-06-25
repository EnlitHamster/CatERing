package businesslogic.shift;

import businesslogic.user.User;

import java.util.Calendar;

public abstract class Shift implements Comparable<Shift>{
    private Calendar date;
    public abstract boolean isAvailable(User u);
    public Shift(Calendar date){
    }

    public Calendar getDate(){return date;}

    @Override
    public int compareTo(Shift o) {
        return date.compareTo(o.date);
    }
}
