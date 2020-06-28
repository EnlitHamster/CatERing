package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.kitchen.KitchenJob;
import businesslogic.kitchen.KitchenJobsException;
import businesslogic.kitchen.KitchenJobsManager;
import businesslogic.kitchen.SummarySheet;
import businesslogic.menu.Section;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ui.shiftBoard.ShiftBoardController;

import java.io.IOException;


public class SummarySheetContent {
    KitchenManagement kitchenManagementController;

    @FXML Button deleteOffMenuRecipeButton;
    @FXML Button downButton;
    @FXML Button upButton;
    @FXML Button setCompletedButton;
    @FXML Button modifyInfoButton;
    @FXML Label serviceLabel;
    @FXML ListView<KitchenJob> jobList;
    @FXML TextField cookField;
    @FXML TextField shiftField;
    @FXML TextField estimateField;
    @FXML TextField quantityField;
    @FXML TextField isCompleteField;
    @FXML Button addJobButton;
    @FXML Button removeJobButton;
    @FXML Button modifyAssignmentButton;
    @FXML Button deleteAssignmentButton;

    private KitchenJobsManager getKitchenManager(){
        return CatERing.getInstance().getKitchenManager();
    }

    public void initialize() {
        SummarySheet toview = getKitchenManager().getCurrentSummarySheet();
        if (toview != null) {
            serviceLabel.setText(toview.getService().getName());
            jobList.setItems(toview.getJobs());
        }
        jobList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        jobList.getSelectionModel().select(null);
        jobList.getSelectionModel().selectedItemProperty().addListener((observableValue, oldJob, newJob) -> {
            removeJobButton.setDisable(newJob == null);
            deleteOffMenuRecipeButton.setDisable(newJob == null);
            downButton.setDisable(newJob == null);
            upButton.setDisable(newJob == null);
            refreshCurrentJob();
        });
    }

    public void setMenuManagementController(KitchenManagement kitchenManagement) {
        kitchenManagementController = kitchenManagement;
    }

    public void exitButtonPressed() {
        kitchenManagementController.showSummarySheetList();
    }

    private KitchenJob getCurrentJob(){
        return jobList.getSelectionModel().getSelectedItem();
    }

    private void refreshCurrentJob(){
        KitchenJob currJob = getCurrentJob();
        if (currJob != null) {
            cookField.setText(currJob.getAssignedCookName());
            shiftField.setText(currJob.getShiftString());
            estimateField.setText(currJob.getEstimateTime());
            quantityField.setText(currJob.getQuantityString());
            isCompleteField.setText(currJob.isComplete() ? "si'" : "no");
            modifyInfoButton.setDisable(false);
            setCompletedButton.setDisable(currJob.isComplete());
            modifyAssignmentButton.setDisable(false);
            deleteAssignmentButton.setDisable(false);
        }
        else{
            cookField.setText("");
            shiftField.setText("");
            estimateField.setText("");
            quantityField.setText("");
            isCompleteField.setText("");
            modifyInfoButton.setDisable(true);
            setCompletedButton.setDisable(true);
            deleteAssignmentButton.setDisable(true);
            modifyAssignmentButton.setDisable(true);
        }
    }

    public void modifyInfoPressed() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("summarySheet-modifyDialog.fxml"));
        try {
            Pane pane = loader.load();
            ModifyDialog controller = loader.getController();
            Stage stage = new Stage();
            controller.init(stage, jobList.getSelectionModel().getSelectedItem());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));
            stage.setTitle("Modifica info compito");
            stage.showAndWait();
            refreshCurrentJob();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setCompletedButtonPressed() {
        try {
            KitchenJob currJob = getCurrentJob();
            getKitchenManager().setJobCompleted(currJob);
            refreshCurrentJob();
        } catch (UseCaseLogicException e) {
            e.printStackTrace();
        }
    }

    public void addJobButtonPressed() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("summarySheet-addTask.fxml"));
        try {
            Pane pane = loader.load();
            AddTaskDialog controller = loader.getController();
            Stage stage = new Stage();
            controller.init(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));
            stage.setTitle("Aggiungi un compito per una ricetta");
            stage.showAndWait();
            refreshCurrentJob();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void removeJobButtonPressed() {
        try {
            getKitchenManager().deleteJob(getCurrentJob());
        } catch (UseCaseLogicException e) {
            e.printStackTrace();
        }
    }

    public void deleteOffMenuRecipe() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("summarySheet-deleteOffMenuTask.fxml"));
        try {
            Pane pane = loader.load();
            DeleteOffMenuTaskDialog controller = loader.getController();
            Stage stage = new Stage();
            controller.init(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));
            stage.setTitle("Elimina una ricetta fuori menu");
            stage.showAndWait();
            refreshCurrentJob();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void changeSectionPosition(int change) {
        int newpos = jobList.getSelectionModel().getSelectedIndex() + change;
        KitchenJob job = jobList.getSelectionModel().getSelectedItem();
        try {
            CatERing.getInstance().getKitchenManager().rearrangeJob(job, newpos);
            //sectionList.refresh();
            jobList.getSelectionModel().select(newpos);
        } catch (UseCaseLogicException | KitchenJobsException ex) {
            ex.printStackTrace();
        }
    }

    public void upButtonPressed() {
        this.changeSectionPosition(-1);
    }

    public void downButtonPressed() {
        this.changeSectionPosition(+1);
    }

    public void shiftBoardButtonPressed() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../shiftBoard/shiftBoard.fxml"));
        try {
            Pane pane = loader.load();
            ShiftBoardController controller = loader.getController();
            Stage stage = new Stage();
            controller.init(stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));
            stage.setTitle("Tabellone turni");
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void modifyAssignmentButtonPressed() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("summarySheet-assignJob.fxml"));
        try {
            Pane pane = loader.load();
            AssignJobDialog controller = loader.getController();
            Stage stage = new Stage();
            controller.init(stage, getCurrentJob());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));
            stage.setTitle("Modifica assegnamento");
            stage.showAndWait();
            refreshCurrentJob();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteAssignmentBUttonPressed() {
        try {
            getKitchenManager().unassignJob(getCurrentJob());
            refreshCurrentJob();
        } catch (UseCaseLogicException e) {
            e.printStackTrace();
        }
    }
}
