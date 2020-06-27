package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.event.EventInfo;
import businesslogic.event.ServiceInfo;
import businesslogic.kitchen.SummarySheet;
import businesslogic.menu.Menu;
import businesslogic.user.User;
import com.sun.javafx.collections.ObservableIntegerArrayImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class SummarySheetList {

    @FXML ListView<ServiceInfo> summarySheetList;
    ObservableList<ServiceInfo> summarySheetItems;

    @FXML ListView<EventInfo> eventList;
    ObservableList<EventInfo> eventListItems;

    @FXML Button newButton;
    @FXML Button openButton;
    @FXML Button deleteButton;

    private KitchenManagement kitchenManagementController;

    public void setParent(KitchenManagement kitchenManagement) {
        kitchenManagementController = kitchenManagement;
    }

    @FXML public void endButtonPressed(){
        System.out.println("." + kitchenManagementController);
        kitchenManagementController.endKitchenManagement();
    }

    public void initialize() {
        if(eventListItems == null){
            eventListItems = CatERing.getInstance().getEventManager().getEventInfo();
            eventList.setItems(eventListItems);
            eventList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            eventList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldEvent, newEvent) -> {
                if(newEvent != null) {
                    summarySheetItems = newEvent.getServices();
                    summarySheetList.getSelectionModel().selectedItemProperty().addListener((obsV, oldService, newService) -> {
                        User u = CatERing.getInstance().getUserManager().getCurrentUser();
                        newButton.setDisable(newService == null || !newService.getEvent().isChef(u) || newService.hasSummarySheet());
                        openButton.setDisable(newService == null || !newService.getEvent().isChef(u) || !newService.hasSummarySheet());
                        deleteButton.setDisable(newService == null || !newService.getEvent().isChef(u) || !newService.hasSummarySheet());
                    });
                }
                else summarySheetItems = FXCollections.observableArrayList();
                summarySheetList.setItems(summarySheetItems);
            });

        }
        else {
            eventList.refresh();
        }
    }
}
