package businesslogic.recipe;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

public class Recipe extends KitchenTask {
    public Recipe(String name) {
        super(name);
    }

    private Recipe() {
        super();
    }

    public static Recipe loadRecipe(ResultSet rs) throws SQLException {
        Recipe rec = new Recipe();
        rec.name = rs.getString("name");
        rec.notes = rs.getString("notes");
        rec.tags = Collections.singletonList(rs.getString("tags"));
        return rec;
    }
}
