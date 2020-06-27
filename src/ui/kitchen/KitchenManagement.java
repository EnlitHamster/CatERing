package ui.kitchen;

import businesslogic.CatERing;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import ui.Main;
import ui.menu.MenuContent;

import java.awt.*;
import java.io.IOException;

public class KitchenManagement {
    @FXML SplitPane summarySheetListPane;
    @FXML BorderPane containerPane;
    @FXML SummarySheetList summarySheetListPaneController;
    @FXML Label userLabel;

    BorderPane summarySheetContentPane;
    SummarySheetContent summarySheetContentPaneController;
    private Main mainPaneController;

    public void setMainPaneController(Main main) {mainPaneController = main;}
    public void endKitchenManagement() {mainPaneController.showStartPane(containerPane);}

    public void initialize(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("summarySheet-content.fxml"));
        try {
            summarySheetContentPane = loader.load();
        } catch(IOException ex) {
            ex.printStackTrace();
        }
        summarySheetContentPaneController = loader.getController();
        summarySheetContentPaneController.setMenuManagementController(this);

        if (CatERing.getInstance().getUserManager().getCurrentUser() != null) {
            String uname = CatERing.getInstance().getUserManager().getCurrentUser().getUserName();
            userLabel.setText(uname);
        }
        summarySheetListPaneController.setParent(this);
    }

    public void showCurrentSummarySheet() {
        summarySheetContentPaneController.initialize();
        containerPane.setCenter(summarySheetContentPane);
    }


}
