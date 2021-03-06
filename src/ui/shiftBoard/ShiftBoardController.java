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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static businesslogic.shift.ShiftManager.month;
import static businesslogic.shift.ShiftManager.monthToInt;

public class ShiftBoardController {
    @FXML Button openButton;
    @FXML ListView<ShiftListItem> shiftList;
    @FXML Button findButton;
    @FXML ComboBox<String> monthComboBox;
    @FXML ComboBox<Integer> dayComboBox;
    private Stage myStage;

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
            Calendar searchDate = Calendar.getInstance();
            searchDate.set(2020, monthToInt(selectedMonth), selectedDay);
            List<KitchenShift> shiftBoard = CatERing.getInstance().getShiftManager().getShiftBoard();
            List<KitchenShift> searchedShift =
                    shiftBoard.stream()
                            .filter(s -> {
                                Calendar shiftDate = Calendar.getInstance();
                                shiftDate.setTimeInMillis(s.getDate().getTime());
                                return shiftDate.get(Calendar.DAY_OF_MONTH) == searchDate.get(Calendar.DAY_OF_MONTH)
                                        && shiftDate.get(Calendar.MONTH) == searchDate.get(Calendar.MONTH);
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
            Calendar shiftDate = Calendar.getInstance();
            shiftDate.setTimeInMillis(s.getDate().getTime());
            if(shiftDate.get(Calendar.HOUR_OF_DAY) == i) return s;
        }
        return null;
    }
}
