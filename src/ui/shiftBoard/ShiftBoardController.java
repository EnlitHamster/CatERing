package ui.shiftBoard;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ShiftBoardController {
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

    public void init(Stage stage) {
        ArrayList<Integer> days = new ArrayList<>();
        for(int i = 1; i <= 31; i++) days.add(i);
        monthComboBox.setItems(FXCollections.observableList(Arrays.asList(month)));
        dayComboBox.setItems(FXCollections.observableList(days));
        myStage = stage;
    }
}
