package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    public void init(Stage stage){
        myStage = stage;
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
}
