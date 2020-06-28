package businesslogic.event;

import businesslogic.kitchen.SummarySheet;
import businesslogic.menu.Menu;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ServiceInfo implements EventItemInfo {
    private int id;
    private String name;
    private Date date;
    private Time timeStart;
    private Time timeEnd;
    private int participants;
    private Menu menu;
    private SummarySheet summarySheet;
    private EventInfo event;

    private ServiceInfo() {}

    public ServiceInfo(EventInfo event, String name) {
        this.event = event;
        this.name = name;
        menu = null;
        summarySheet = null;
    }

    public Integer getId() {
        return id;
    }

    public EventInfo getEvent() {
        return event;
    }

    public Menu getMenu() {
        return menu;
    }

    public boolean hasSummarySheet() {
        return summarySheet != null;
    }

    public SummarySheet getSummarySheet() {
        return summarySheet;
    }

    public void setSummarySheet(SummarySheet sheet) {
        summarySheet = sheet;
    }

    public String getName(){return name;}

    public String toString() {
        return name + ": " + date + " (" + timeStart + "-" + timeEnd + "), " + participants + " pp.";
    }

    // STATIC METHODS FOR PERSISTENCE

    public static ObservableList<ServiceInfo> loadServiceInfoForEvent(EventInfo event) {
        ObservableList<ServiceInfo> result = FXCollections.observableArrayList();
        String query = "SELECT id, name, service_date, time_start, time_end, expected_participants, approved_menu_id " +
                "FROM services WHERE event_id = " + event.getID();
        var obj = new Object() {List<Integer> mids = new ArrayList<>();};
        PersistenceManager.executeQuery(query, rs -> {
            ServiceInfo serv = new ServiceInfo();
            serv.event = event;
            serv.name = rs.getString("name");
            serv.id = rs.getInt("id");
            serv.date = rs.getDate("service_date");
            serv.timeStart = rs.getTime("time_start");
            serv.timeEnd = rs.getTime("time_end");
            serv.participants = rs.getInt("expected_participants");
            obj.mids.add(rs.getInt("approved_menu_id"));
            result.add(serv);
        });
        for (int i = 0; i < result.size(); ++i)
            if (obj.mids.get(i) != 0) result.get(i).menu = Menu.loadMenuById(obj.mids.get(i));

        for (ServiceInfo serv : result)
            serv.summarySheet = SummarySheet.loadSummarySheetByService(serv);

        return result;
    }

    public boolean hasMenu() {
        return menu != null;
    }
}
