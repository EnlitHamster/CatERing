package ui.shiftBoard;

import businesslogic.CatERing;
import businesslogic.shift.KitchenShift;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ShiftBoardController {
    @FXML Button openButton;
    @FXML ListView<ShiftListItem> shiftList;
    @FXML Button findButton;
    @FXML ComboBox<String> monthComboBox;
    @FXML ComboBox<Integer> dayComboBox;
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

    public void init(Stage stage) {
        ArrayList<Integer> days = new ArrayList<>();
        for(int i = 1; i <= 31; i++) days.add(i);
        monthComboBox.setItems(FXCollections.observableList(Arrays.asList(month)));
        dayComboBox.setItems(FXCollections.observableList(days));
        myStage = stage;
        shiftList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        shiftList.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldShift, newShift) -> {
            if(newShift == null) openButton.setDisable(true);
            else openButton.setDisable(newShift.isEmpty());
        }));
    }

    private KitchenShift searchedShift;

    public void monthSelected() {
        findButton.setDisable(monthComboBox.getValue() == null || dayComboBox.getValue() == null);
    }

    public void daySelected() {
        findButton.setDisable(monthComboBox.getValue() == null || dayComboBox.getValue() == null);
    }

    public void close() {
        myStage.close();
    }

    public void openButtonPressed() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../shiftBoard/shiftInfo.fxml"));
        try {
            Pane pane = loader.load();
            ShiftInfoController controller = loader.getController();
            Stage stage = new Stage();
            controller.init(stage, shiftList.getSelectionModel().getSelectedItem());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane));
            stage.setTitle("Info turno");
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void findButtonPressed() {
        String selectedMonth = monthComboBox.getValue();
        Integer selectedDay = dayComboBox.getValue();
        if(selectedMonth != null && selectedDay != null){
            Date searchDate = new Date(2020,  monthToInt(selectedMonth),selectedDay);
            List<KitchenShift> shiftBoard = CatERing.getInstance().getShiftManager().getShiftBoard();
            List<KitchenShift> searchedShift =
                    shiftBoard.stream()
                            .filter(s -> {
                                Date shiftDate = new Date(s.getDate().getTime());
                                return shiftDate.getDay() == searchDate.getDay()
                                        && shiftDate.getMonth() == searchDate.getMonth();
                            }).collect(Collectors.toList());
            ObservableList<ShiftListItem> shiftListItems = FXCollections.observableArrayList();
            for (int i = 0; i <= 23; i++){
                ShiftListItem item = new ShiftListItem(i + ":00");
                KitchenShift referredShift = findShiftThatMatchHour(searchedShift, i);
                item.setRefferredShift(referredShift);
                shiftListItems.add(item);
            }
            shiftList.setItems(shiftListItems);
        }
    }

    private KitchenShift findShiftThatMatchHour(List<KitchenShift> searchedShift, int i) {
        for(KitchenShift s: searchedShift){
            Date shiftDate = new Date(s.getDate().getTime());
            if(shiftDate.getHours() == i) return s;
        }
        return null;
    }
}
