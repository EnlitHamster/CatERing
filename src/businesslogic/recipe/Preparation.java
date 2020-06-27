package businesslogic.recipe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

public class Preparation extends KitchenTask {
    public Preparation(String name) {
        super(name);
    }

    private Preparation() {
        super();
    }

    public static Preparation loadPreparation(ResultSet rs) throws SQLException {
        Preparation prep = new Preparation();
        prep.name = rs.getString("name");
        prep.notes = rs.getString("notes");
        prep.tags = Collections.singletonList(rs.getString("tags"));
        return prep;
    }
}
