package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.EventInfo;
import businesslogic.event.ServiceInfo;
import businesslogic.kitchen.KitchenJobsException;
import businesslogic.kitchen.SummarySheet;
import businesslogic.menu.Menu;
import businesslogic.user.User;
import com.sun.javafx.collections.ObservableIntegerArrayImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
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

    @FXML
    public void newButtonPressed() {
        ServiceInfo currentService = summarySheetList.getSelectionModel().getSelectedItem();
        try{
            SummarySheet s = CatERing.getInstance().getKitchenManager().createSummarySheet(currentService);
        }
        catch (UseCaseLogicException | KitchenJobsException e) {
            e.printStackTrace();
        }
        kitchenManagementController.showCurrentSummarySheet();
        eventList.refresh();
        refreshSummarySheetList();
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
                        newButton.setDisable(
                                newService == null || !newService.getEvent().isChef(u)
                                || newService.hasSummarySheet() || !newService.hasMenu()
                        );
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

    private void refreshSummarySheetList(){
        summarySheetItems = eventList.getSelectionModel().getSelectedItem().getServices();
        summarySheetList.setItems(summarySheetItems);
    }

    public void deleteButtonPressed(ActionEvent actionEvent) {
        ServiceInfo currentService = summarySheetList.getSelectionModel().getSelectedItem();
        try{
            CatERing.getInstance().getKitchenManager().deleteSummarySheet(currentService);
        }
        catch (UseCaseLogicException | KitchenJobsException e) {
            e.printStackTrace();
        }
        eventList.refresh();
        refreshSummarySheetList();
    }

    public void openButtonPressed(ActionEvent actionEvent) {
        ServiceInfo currentService = summarySheetList.getSelectionModel().getSelectedItem();
        try{
            CatERing.getInstance().getKitchenManager().selectSummarySheet(currentService);
        }
        catch (UseCaseLogicException | KitchenJobsException e) {
            e.printStackTrace();
        }
        kitchenManagementController.showCurrentSummarySheet();
        eventList.refresh();
        refreshSummarySheetList();;
    }
}
