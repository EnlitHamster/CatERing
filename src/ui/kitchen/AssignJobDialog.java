package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.kitchen.KitchenJob;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AssignJobDialog {
    @FXML ComboBox<User> cookComboBox;
    @FXML ComboBox<String> monthComboBox;
    @FXML ComboBox<Integer> dayComboBox;
    @FXML ComboBox<Integer> hourComboBox;
    private Stage myStage;
    private KitchenJob job;

    private static String[] month = {
            "Gen",
            "Feb",
            "Mar",
            "Apr",
            "Mag",
            "Giu",
            "Lug",
            "Ago",
            "Set",
            "Ott",
            "Nov",
            "Dic"
    };

    private static int monthToInt(String month){
        switch (month) {
            case "Gen":
                return Calendar.JANUARY;
            case "Feb":
                return Calendar.FEBRUARY;
            case "Mar":
                return Calendar.MARCH;
            case "Apr":
                return Calendar.APRIL;
            case "Mag":
                return Calendar.MAY;
            case "Giu":
                return Calendar.JUNE;
            case "Lug":
                return Calendar.JULY;
            case "Ago":
                return Calendar.AUGUST;
            case "Set":
                return Calendar.SEPTEMBER;
            case "Ott":
                return Calendar.OCTOBER;
            case "Nov":
                return Calendar.NOVEMBER;
            case "Dic":
                return Calendar.DECEMBER;
            default:
                return -1;
        }
    }

    public void init(Stage stage, KitchenJob workingJob){
        myStage = stage;
        job = workingJob;
        monthComboBox.setItems(FXCollections.observableList(Arrays.asList(month)));
        ArrayList<Integer> days = new ArrayList<>();
        for (int i = 1; i <= 31; i++) days.add(i);
        dayComboBox.setItems(FXCollections.observableList(days));
        ArrayList<Integer> hours = new ArrayList<>();
        for (int i = 0; i <= 23; i++) hours.add(i);
        hourComboBox.setItems(FXCollections.observableList(hours));
        List<User> cooks = CatERing.getInstance().getUserManager().getAllCooks();
        cookComboBox.setItems(FXCollections.observableList(cooks));
    }

    public void cancelButtonPressed(ActionEvent actionEvent) {
        myStage.close();
    }

    public void assignButtonPressed(ActionEvent actionEvent) {
        User cookToAssign = cookComboBox.getValue();
        String selectedMonth = monthComboBox.getValue();
        Integer selectedDay = dayComboBox.getValue();
        Integer selectedHour = hourComboBox.getValue();
        boolean error = false;
        if(selectedDay == null || selectedMonth == null || selectedHour == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Mese, giorno e ora devono essere selezionati");
            alert.showAndWait();
            error = true;
        }
        if(!error){
            //shift = creare un turno
            //CatERing.getInstance().getKitchenManager().assignJob(job, cookToAssign, shiftToAssign);
        }
    }
}
