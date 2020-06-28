package businesslogic.user;

import javafx.collections.FXCollections;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class User {

    private static final Map<Integer, User> loadedUsers = FXCollections.observableHashMap();

    public enum Role {SERVIZIO, CUOCO, CHEF, ORGANIZZATORE}

    private int id;
    private String username;
    private final Set<Role> roles;

    public User() {
        id = 0;
        username = "";
        this.roles = new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean isChef() {
        return roles.contains(Role.CHEF);
    }

    public boolean isCook() {
        return roles.contains(Role.CUOCO);
    }

    public String getUserName() {
        return username;
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        String result = username;
        if (roles.size() > 0) {
            result += ": ";

            for (User.Role r : roles) {
                result += r.toString() + " ";
            }
        }
        return result;
    }

    // STATIC METHODS FOR PERSISTENCE

    public static List<User> loadAllCooks(){
        List<User> loadedCooks = new ArrayList<User>();
        String userQuery =
                "SELECT id, username FROM Users, UserRoles " +
                "WHERE id = user_id and role_id = 'c'";
        PersistenceManager.executeQuery(userQuery, rs -> {
            User cook = new User();
            cook.id = rs.getInt("id");
            cook.username = rs.getString("username");
            cook.roles.add(Role.CUOCO);
            loadedCooks.add(cook);
        });
        return loadedCooks;
    }

    public static User loadUserById(int uid) {
        if (uid == 0) return null;
        if (loadedUsers.containsKey(uid)) return loadedUsers.get(uid);

        User load = new User();
        String userQuery = "SELECT * FROM users WHERE id='"+uid+"'";
        PersistenceManager.executeQuery(userQuery, rs -> {
            load.id = rs.getInt("id");
            load.username = rs.getString("username");
        });
        if (load.id > 0) {
            loadedUsers.put(load.id, load);
            String roleQuery = "SELECT * FROM userroles WHERE user_id=" + load.id;
            PersistenceManager.executeQuery(roleQuery, rs -> {
                String role = rs.getString("role_id");
                switch (role.charAt(0)) {
                    case 'c':
                        load.roles.add(Role.CUOCO);
                        break;
                    case 'h':
                        load.roles.add(Role.CHEF);
                        break;
                    case 'o':
                        load.roles.add(Role.ORGANIZZATORE);
                        break;
                    case 's':
                        load.roles.add(Role.SERVIZIO);
                }
            });
        }
        return load;
    }

    public static User loadUser(String username) {
        User u = new User();
        String userQuery = "SELECT * FROM users WHERE username='"+username+"'";
        PersistenceManager.executeQuery(userQuery, rs -> {
            u.id = rs.getInt("id");
            u.username = rs.getString("username");
        });
        if (u.id > 0) {
            loadedUsers.put(u.id, u);
            String roleQuery = "SELECT * FROM userroles WHERE user_id=" + u.id;
            PersistenceManager.executeQuery(roleQuery, rs -> {
                String role = rs.getString("role_id");
                switch (role.charAt(0)) {
                    case 'c':
                        u.roles.add(Role.CUOCO);
                        break;
                    case 'h':
                        u.roles.add(Role.CHEF);
                        break;
                    case 'o':
                        u.roles.add(Role.ORGANIZZATORE);
                        break;
                    case 's':
                        u.roles.add(Role.SERVIZIO);
                }
            });
        }
        return u;
    }
}
