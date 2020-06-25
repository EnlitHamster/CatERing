import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.menu.Menu;
import businesslogic.menu.Section;
import businesslogic.recipe.KitchenTask;
import javafx.collections.ObservableList;

public class TestCatERing2b {
    public static void main(String[] args) {
        try {
            /* System.out.println("TEST DATABASE CONNECTION");
            PersistenceManager.testSQLConnection();*/
            CatERing.getInstance().getUserManager().fakeLogin("Lidia");
            System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());
            Menu m = CatERing.getInstance().getMenuManager().createMenu("Menu Pinco Pallino");
            Section sec = CatERing.getInstance().getMenuManager().defineSection("Antipasti");

            ObservableList<KitchenTask> kitchenTasks = CatERing.getInstance().getRecipeManager().getRecipes();
            for (int i = 0; i < 4 && i < kitchenTasks.size(); i++) {
                CatERing.getInstance().getMenuManager().insertItem(kitchenTasks.get(i), sec);
            }
            for (int i = kitchenTasks.size()-1; i > kitchenTasks.size()-4 && i >= 0; i--) {
                CatERing.getInstance().getMenuManager().insertItem(kitchenTasks.get(i));
            }
            System.out.println(m.testString());

            System.out.println("\nTEST DELETE SECTION BUT KEEP ITEMS");
            CatERing.getInstance().getMenuManager().deleteSection(sec, false);
            System.out.println(m.testString());

        } catch (UseCaseLogicException ex) {
            System.out.println("Errore di logica nello use case");
        }
    }
}