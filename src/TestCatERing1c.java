import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.menu.Menu;
import businesslogic.menu.Section;
import businesslogic.recipe.KitchenTask;
import businesslogic.recipe.Recipe;
import javafx.collections.ObservableList;

public class TestCatERing1c {
    public static void main(String[] args) {
        try {
            /* System.out.println("TEST DATABASE CONNECTION");
            PersistenceManager.testSQLConnection();*/
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

            Menu m = CatERing.getInstance().getMenuManager().createMenu("Menu Pinco Pallino");

            Section sec = CatERing.getInstance().getMenuManager().defineSection("Antipasti");

            ObservableList<KitchenTask> kitchenTasks = CatERing.getInstance().getKitchenTaskManager().getKitchenTasks();
            for (int i = 0; i < 4 && i < kitchenTasks.size(); i++) {
                CatERing.getInstance().getMenuManager().insertItem((Recipe) kitchenTasks.get(i), sec);
            }

            for (int i = kitchenTasks.size()-1; i > kitchenTasks.size()-4 && i >= 0; i--) {
                CatERing.getInstance().getMenuManager().insertItem((Recipe) kitchenTasks.get(i));
            }

            CatERing.getInstance().getMenuManager().publish();
            System.out.println("\nMENU ORIGINALE");
            System.out.println(m.testString());

            System.out.println("\nTEST COPIA");
            Menu m2 = CatERing.getInstance().getMenuManager().copyMenu(m);
            System.out.println(m2.testString());

        } catch (UseCaseLogicException ex) {
            System.out.println("Errore di logica nello use case");
        }
    }
}
