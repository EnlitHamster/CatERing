package businesslogic;

import businesslogic.event.EventManager;
import businesslogic.kitchen.KitchenJobsManager;
import businesslogic.menu.MenuManager;
import businesslogic.recipe.KitchenTaskManager;
import businesslogic.shift.ShiftManager;
import businesslogic.user.UserManager;
import persistence.KitchenJobsPersistence;
import persistence.MenuPersistence;
import persistence.ShiftPersistence;

public class CatERing {
    private static CatERing singleInstance;

    public static CatERing getInstance() {
        if (singleInstance == null) {
            singleInstance = new CatERing();
        }
        return singleInstance;
    }

    private final MenuManager menuMgr;
    private final KitchenTaskManager recipeMgr;
    private final UserManager userMgr;
    private final EventManager eventMgr;
    private final KitchenJobsManager kitchenMgr;

    private final MenuPersistence menuPersistence;
    private final ShiftPersistence shiftPersistence;
    private final KitchenJobsPersistence kitchenJobsPersistence;

    private CatERing() {
        menuMgr = new MenuManager();
        recipeMgr = new KitchenTaskManager();
        userMgr = new UserManager();
        eventMgr = new EventManager();
        kitchenMgr = new KitchenJobsManager();
        menuPersistence = new MenuPersistence();
        shiftPersistence = new ShiftPersistence();
        kitchenJobsPersistence = new KitchenJobsPersistence();
        menuMgr.addEventReceiver(menuPersistence);
        // TODO: Uncomment for persistence
        //ShiftManager.getInstance().addEventReceiver(shiftPersistence);
        //kitchenMgr.addEventReceiver(kitchenJobsPersistence);
    }

    public MenuManager getMenuManager() {
        return menuMgr;
    }

    public KitchenJobsManager getKitchenManager(){
        return kitchenMgr;
    }

    public KitchenTaskManager getKitchenTaskManager() {
        return recipeMgr;
    }

    public UserManager getUserManager() {
        return userMgr;
    }

    public EventManager getEventManager() {
        return eventMgr;
    }

    public ShiftManager getShiftManager() {
        return ShiftManager.getInstance();
    }

}
