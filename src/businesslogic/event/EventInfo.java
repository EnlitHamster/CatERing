package businesslogic.event;

import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;

import java.sql.Date;

public class EventInfo implements EventItemInfo {
    private Integer id;
    private String name;
    private Date dateStart;
    private Date dateEnd;
    private Integer participants;
    private User organizer;
    private User chef;

    private ObservableList<ServiceInfo> services;

    public EventInfo(String name) {
        this.name = name;
        id = 0;
    }

    public Integer getID() {
        return id;
    }

    public ObservableList<ServiceInfo> getServices() {
        return FXCollections.unmodifiableObservableList(this.services);
    }

    public String toString() {
        return name + ": " + dateStart + "-" + dateEnd + ", " + participants + " pp. (" + organizer.getUserName() + ")";
    }

    public boolean containsService(ServiceInfo service) {
        return services.contains(service);
    }

    public boolean isChef(User u) {
        return chef != null && u != null && chef.getId() == u.getId();
    }

    // STATIC METHODS FOR PERSISTENCE

    public static ObservableList<EventInfo> loadAllEventInfo() {
        ObservableList<EventInfo> all = FXCollections.observableArrayList();
        String query = "SELECT * FROM Events WHERE true";
        PersistenceManager.executeQuery(query, rs -> {
            String n = rs.getString("name");
            EventInfo e = new EventInfo(n);
            e.id = rs.getInt("id");
            e.dateStart = rs.getDate("date_start");
            e.dateEnd = rs.getDate("date_end");
            e.participants = rs.getInt("expected_participants");
            int org = rs.getInt("organizer_id");
            int chefId = rs.getInt("chef");
            e.organizer = User.loadUserById(org);
            e.chef = User.loadUserById(chefId);
            all.add(e);
        });

        for (EventInfo e : all) {
            e.services = ServiceInfo.loadServiceInfoForEvent(e);
        }
        return all;
    }
}
