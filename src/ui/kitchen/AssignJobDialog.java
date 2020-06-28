package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.kitchen.KitchenJob;
import businesslogic.shift.KitchenShift;
import businesslogic.user.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static businesslogic.shift.ShiftManager.month;
import static businesslogic.shift.ShiftManager.monthToInt;

public class AssignJobDialog {
    @FXML ComboBox<User> cookComboBox;
    @FXML ComboBox<String> monthComboBox;
    @FXML ComboBox<Integer> dayComboBox;
    @FXML ComboBox<Integer> hourComboBox;
    private Stage myStage;
    private KitchenJob job;

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
        if(!error) {
            Calendar shiftDate = Calendar.getInstance();
            shiftDate.set(Calendar.MONTH, monthToInt(selectedMonth));
            shiftDate.set(Calendar.DAY_OF_MONTH, selectedDay);
            shiftDate.set(Calendar.HOUR_OF_DAY, selectedHour);
            KitchenShift shift = CatERing.getInstance().getShiftManager().getKitchenShift(shiftDate);
            if (shift == null) shift = new KitchenShift(new Timestamp(shiftDate.getTimeInMillis()));


            //shift = creare un turno
            //CatERing.getInstance().getKitchenManager().assignJob(job, cookToAssign, shiftToAssign);
        }
    }
}
