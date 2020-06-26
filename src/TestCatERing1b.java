import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.menu.Menu;
import businesslogic.menu.MenuException;
import businesslogic.menu.Section;
import businesslogic.recipe.KitchenTask;
import businesslogic.recipe.Recipe;
import javafx.collections.ObservableList;

public class TestCatERing1b {
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
                CatERing.getInstance().getMenuManager().insertItem((Recipe) kitchenTasks.get(i), sec);
            }

            for (int i = kitchenTasks.size()-1; i > kitchenTasks.size()-4 && i >= 0; i--) {
                CatERing.getInstance().getMenuManager().insertItem((Recipe) kitchenTasks.get(i));
            }

            CatERing.getInstance().getMenuManager().publish();
            System.out.println("\nMENU CREATO");
            System.out.println(m.testString());

            System.out.println("\nTEST DELETE");
            CatERing.getInstance().getMenuManager().deleteMenu(m);

        } catch (UseCaseLogicException | MenuException e) {
            System.out.println("Errore di logica nello use case");
        }
    }
}
