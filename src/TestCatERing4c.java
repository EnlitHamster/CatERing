import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.menu.Menu;
import businesslogic.menu.MenuItem;
import businesslogic.menu.Section;
import businesslogic.recipe.KitchenTask;
import businesslogic.recipe.Recipe;
import javafx.collections.ObservableList;

public class TestCatERing4c {
    public static void main(String[] args) {
        try {
            /* System.out.println("TEST DATABASE CONNECTION");
            PersistenceManager.testSQLConnection();*/
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());
            Menu m = CatERing.getInstance().getMenuManager().createMenu("Menu Pinco Pallino");
            Section sec = CatERing.getInstance().getMenuManager().defineSection("Antipasti");

            ObservableList<KitchenTask> kitchenTasks = CatERing.getInstance().getKitchenTaskManager().getKitchenTasks();
            MenuItem[] its = new MenuItem[4];
            for (int i = 0; i < 4 && i < kitchenTasks.size(); i++) {
                its[i] = CatERing.getInstance().getMenuManager().insertItem((Recipe) kitchenTasks.get(i), sec);
            }

            MenuItem[] its2 = new MenuItem[3];
            for (int i = kitchenTasks.size()-1; i > kitchenTasks.size()-4 && i >= 0; i--) {
                its2[kitchenTasks.size()-1-i] = CatERing.getInstance().getMenuManager().insertItem((Recipe) kitchenTasks.get(i));

            }
            CatERing.getInstance().getMenuManager().defineSection("Primi");
            CatERing.getInstance().getMenuManager().defineSection("Secondi");
            System.out.println(m.testString());

            System.out.println("\nTEST REMOVE ITEM");
            CatERing.getInstance().getMenuManager().deleteItem(its[2]);
            CatERing.getInstance().getMenuManager().deleteItem(its2[0]);
            System.out.println(m.testString());

        } catch (UseCaseLogicException ex) {
            System.out.println("Errore di logica nello use case");
        }
    }
}
