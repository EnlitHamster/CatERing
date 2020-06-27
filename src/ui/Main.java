package ui;

import businesslogic.CatERing;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import ui.kitchen.KitchenManagement;
import ui.menu.MenuManagement;

import java.io.IOException;

public class Main {

    @FXML
    AnchorPane paneContainer;

    @FXML
    FlowPane startPane;

    @FXML
    Start startPaneController;

    BorderPane menuManagementPane;
    MenuManagement menuManagementPaneController;

    BorderPane kitchenManagementPane;
    KitchenManagement kitchenManagementPaneController;

    public void initialize() {
        startPaneController.setParent(this);
        FXMLLoader menuManagementLoader = new FXMLLoader(getClass().getResource("menu/menu-management.fxml"));
        FXMLLoader kitchenManagementLoader = new FXMLLoader(getClass().getResource("kitchen/summarySheet-management.fxml"));
        try {
            menuManagementPane = menuManagementLoader.load();
            menuManagementPaneController = menuManagementLoader.getController();
            menuManagementPaneController.setMainPaneController(this);
            kitchenManagementPane = kitchenManagementLoader.load();
            kitchenManagementPaneController = kitchenManagementLoader.getController();
            kitchenManagementPaneController.setMainPaneController(this);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void startMenuManagement() {
        CatERing.getInstance().getUserManager().fakeLogin("Tony");

        menuManagementPaneController.initialize();
        paneContainer.getChildren().remove(startPane);
        paneContainer.getChildren().add(menuManagementPane);
        AnchorPane.setTopAnchor(menuManagementPane, 0.0);
        AnchorPane.setBottomAnchor(menuManagementPane, 0.0);
        AnchorPane.setLeftAnchor(menuManagementPane, 0.0);
        AnchorPane.setRightAnchor(menuManagementPane, 0.0);
    }

    public void startKitchenManagement(){
        CatERing.getInstance().getUserManager().fakeLogin("Tony");

        kitchenManagementPaneController.initialize();
        paneContainer.getChildren().remove(startPane);
        paneContainer.getChildren().add(kitchenManagementPane);
        AnchorPane.setTopAnchor(kitchenManagementPane, 0.0);
        AnchorPane.setBottomAnchor(kitchenManagementPane, 0.0);
        AnchorPane.setLeftAnchor(kitchenManagementPane, 0.0);
        AnchorPane.setRightAnchor(kitchenManagementPane, 0.0);
    }

    public void showStartPane(Pane toRemove) {
        startPaneController.initialize();
        paneContainer.getChildren().remove(toRemove);
        paneContainer.getChildren().add(startPane);
    }
}
