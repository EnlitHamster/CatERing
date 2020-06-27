package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.kitchen.SummarySheet;
import businesslogic.menu.Menu;
import businesslogic.user.User;
import com.sun.javafx.collections.ObservableIntegerArrayImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class SummarySheetList {

    @FXML ListView<Integer> summarySheetList;
    ObservableList<Integer> summarySheetItems;
    private KitchenManagement kitchenManagementController;

    public void setParent(KitchenManagement kitchenManagement) {
        kitchenManagementController = kitchenManagement;
    }

    @FXML public void endButtonPressed(){
        System.out.println("." + kitchenManagementController);
        kitchenManagementController.endKitchenManagement();
    }

    public void initialize() {
        if (summarySheetItems == null) {
            summarySheetItems = CatERing.getInstance().getKitchenManager().getAllSummarySheet();
            summarySheetList.setItems(summarySheetItems);
            summarySheetList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            /*summarySheetList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldMenu, newMenu) -> {
                User u = CatERing.getInstance().getUserManager().getCurrentUser();
                System.out.println(u.getId());

                eliminaButton.setDisable(newMenu == null || newMenu.isInUse() || !newMenu.isOwner(u));
                apriButton.setDisable(newMenu == null || newMenu.isInUse() || !newMenu.isOwner(u));
                copiaButton.setDisable(newMenu == null);
            });*/
        } else {
            summarySheetList.refresh();
        }
    }
}
