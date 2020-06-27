package ui.kitchen;

import businesslogic.CatERing;
import businesslogic.kitchen.SummarySheet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import ui.menu.MenuManagement;


public class SummarySheetContent {
    KitchenManagement kitchenManagementController;

    @FXML Label serviceLabel;
    @FXML ListView jobList;

    public void initialize() {
        SummarySheet toview = CatERing.getInstance().getKitchenManager().getCurrentSummarySheet();
        if (toview != null) {
            serviceLabel.setText(toview.getService().getName());
            jobList.setItems(toview.get());
        }

        sectionList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        sectionList.getSelectionModel().select(null);
        sectionList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Section>() {
            @Override
            public void changed(ObservableValue<? extends Section> observableValue, Section oldSection, Section newSection) {
                if (newSection != null && newSection != oldSection) {
                    if (!paneVisible) {
                        centralPane.getChildren().remove(emptyPane);
                        centralPane.add(itemsPane, 1, 0);
                        paneVisible = true;
                    }
                    itemsTitle.setText("Voci di " + newSection.getName());
                    freeItemsToggle.setSelected(false);
                    itemsList.setItems(newSection.getItems());
                    // enable other section actions
                    addItemButton.setDisable(false);
                    deleteSectionButton.setDisable(false);
                    editSectionButton.setDisable(false);
                    int pos = sectionList.getSelectionModel().getSelectedIndex();
                    upSectionButton.setDisable(pos <= 0);
                    downSectionButton.setDisable(pos >= (CatERing.getInstance().getMenuManager().getCurrentMenu().getSectionCount()-1));
                } else if (newSection == null) {
                    // disable section actions
                    deleteSectionButton.setDisable(true);
                    editSectionButton.setDisable(true);
                    upSectionButton.setDisable(true);
                    downSectionButton.setDisable(true);
                }
            }
        });

        itemsList.setItems(FXCollections.emptyObservableList());
        itemsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        itemsList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MenuItem>() {
            @Override
            public void changed(ObservableValue<? extends MenuItem> observableValue, MenuItem oldItem, MenuItem newItem) {
                if (newItem != null && newItem != oldItem) {
                    int pos = itemsList.getSelectionModel().getSelectedIndex();
                    int count = 0;
                    if (freeItemsToggle.isSelected()) count = CatERing.getInstance().getMenuManager().getCurrentMenu().getFreeItemCount();
                    else {
                        Section sec = sectionList.getSelectionModel().getSelectedItem();
                        if (sec != null) {
                            count = sec.getItemsCount();
                        }
                    }
                    upItemButton.setDisable(pos <= 0);
                    downItemButton.setDisable(pos >= (count-1));
                    spostaItemButton.setDisable(false);
                    modificaItemButton.setDisable(false);
                    deleteItem.setDisable(false);
                } else if (newItem == null) {
                    upItemButton.setDisable(true);
                    downItemButton.setDisable(true);
                    spostaItemButton.setDisable(true);
                    modificaItemButton.setDisable(true);
                    deleteItem.setDisable(true);
                }
            }
        });
        emptyPane = new BorderPane();
        centralPane.getChildren().remove(itemsPane);
        centralPane.add(emptyPane, 1, 0);
        paneVisible = false;

        freeItemsToggle.setSelected(false);
    }

    public void setMenuManagementController(KitchenManagement kitchenManagement) {
        kitchenManagementController = kitchenManagement;
    }

    public void exitButtonPressed(ActionEvent actionEvent) {
        kitchenManagementController.showSummarySheetList();
    }
}
