import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.EventInfo;
import businesslogic.event.ServiceInfo;
import businesslogic.menu.Menu;
import businesslogic.menu.Section;
import businesslogic.recipe.KitchenTask;
import businesslogic.recipe.Recipe;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.Map;

public class TestCatERing {
    public static void main(String[] args) {
        try {
            /* System.out.println("TEST DATABASE CONNECTION");
            PersistenceManager.testSQLConnection();*/
            System.out.println("TEST FAKE LOGIN");
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

            System.out.println("\nTEST CREATE MENU");
            Menu m = CatERing.getInstance().getMenuManager().createMenu("Menu Pinco Pallino");

            System.out.println("\nTEST DEFINE SECTION");
            Section sec = CatERing.getInstance().getMenuManager().defineSection("Antipasti");
            System.out.println(m.testString());

            System.out.println("\nTEST GET EVENT INFO");
            ObservableList<EventInfo> events = CatERing.getInstance().getEventManager().getEventInfo();
            for (EventInfo e: events) {
                System.out.println(e);
                for (ServiceInfo s: e.getServices()) {
                    System.out.println("\t" + s);
                }
            }
            System.out.println("");

            System.out.println("\nTEST INSERT ITEM IN SECTION");
            ObservableList<KitchenTask> kitchenTasks = CatERing.getInstance().getKitchenTaskManager().getKitchenTasks();
            for (int i = 0; i < 4 && i < kitchenTasks.size(); i++) {
                CatERing.getInstance().getMenuManager().insertItem((Recipe) kitchenTasks.get(i), sec);
            }

            System.out.println("\nTEST INSERT FREE ITEM");
            for (int i = kitchenTasks.size()-1; i > kitchenTasks.size()-4 && i >= 0; i--) {
                CatERing.getInstance().getMenuManager().insertItem((Recipe) kitchenTasks.get(i));
            }
            System.out.println(m.testString());

            System.out.println("\nTEST EDIT FEATURES");
            Map<String, Boolean> f = CatERing.getInstance().getMenuManager().getCurrentMenu().getFeatures();
            String[] fNames = f.keySet().toArray(new String[0]);
            boolean[] vals = new boolean[fNames.length];
            Arrays.fill(vals, true);
            CatERing.getInstance().getMenuManager().setAdditionalFeatures(fNames, vals);
            System.out.println(m.testString());

            System.out.println("\nTEST EDIT TITLE");
            CatERing.getInstance().getMenuManager().changeTitle("Obladì Obladà");
            System.out.println(m.testString());

            System.out.println("\nTEST PUBLISH");
            CatERing.getInstance().getMenuManager().publish();
            System.out.println(m.testString());

        } catch (UseCaseLogicException e) {
            System.out.println("Errore di logica nello use case");
        }
    }
}
