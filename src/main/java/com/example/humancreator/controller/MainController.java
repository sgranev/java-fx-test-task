package com.example.humancreator.controller;

import com.example.humancreator.Application;
import com.example.humancreator.dto.Human;
import com.example.humancreator.event.ItemEditEvent;
import com.example.humancreator.repository.JdbcDao;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;


public class MainController {
    @FXML
    private TreeTableView<Human> treeTableView;

    @FXML
    private TreeTableColumn<Human, String> nameColumn;
    @FXML
    private TreeTableColumn<Human, Integer> ageColumn;
    @FXML
    private TreeTableColumn<Human, LocalDate> birthdayColumn;

    private final JdbcDao dao = new JdbcDao();


    public void initialize() {
        nameColumn.setCellValueFactory(param -> param.getValue().getValue().getName());
        ageColumn.setCellValueFactory(param -> param.getValue().getValue().getAge().asObject());
        birthdayColumn.setCellValueFactory(param -> param.getValue().getValue().getBirthday());

        treeTableView.setRoot(new TreeItem<>());
        treeTableView.setShowRoot(false);
        treeTableView.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                TreeItem<Human> selectedItem = treeTableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null && isDateToday(selectedItem.getValue().getBirthday().getValue())) {
                    showBirthdayMessage();
                }
            }
        });

        treeTableView.getRoot().getChildren().addAll(dao.findAll().stream().map(TreeItem::new).collect(Collectors.toList()));

        treeTableView.getSortOrder().add(nameColumn);
        if (treeTableView.getRoot().getChildren().size() > 0) {
            treeTableView.sort();
        }
    }

    @FXML
    void onAdd() throws IOException {
        showEditForm(new TreeItem<>(new Human()));
    }

    @FXML
    void onEdit() throws IOException {
        TreeItem<Human> selectedItem = treeTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            showEditForm(selectedItem);
        } else {
            showNonSelectedItemWarning();
        }
    }

    @FXML
    void onDelete() {
        TreeItem<Human> selectedItem = treeTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            dao.deleteFromDb(selectedItem.getValue());
            treeTableView.getRoot().getChildren().remove(selectedItem);
            treeTableView.getSelectionModel().clearSelection();
        } else {
            showNonSelectedItemWarning();
        }
    }

    private void showEditForm(TreeItem<Human> itemToEdit) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("edit-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        EditFormController controller = fxmlLoader.getController();
        controller.setHumanToEdit(itemToEdit.getValue());

        Stage stage = new Stage();
        stage.addEventHandler(ItemEditEvent.ITEM_SAVED, event -> onItemSaved(itemToEdit));
        stage.setScene(scene);
        stage.show();
    }

    private void showNonSelectedItemWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Selection Alert");
        alert.setHeaderText("Select an Item!");
        alert.setContentText("No item has been selected in the table! Please choose an item to Edit/Delete");
        alert.showAndWait();
    }

    private void showBirthdayMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Birthday today");
        alert.setHeaderText("Happy Birthday, man!");
        alert.setContentText("This human is celebrating their birthday today");
        alert.showAndWait();
    }

    private boolean isDateToday(LocalDate date) {
        LocalDate today = LocalDate.now();
        return date.getMonthValue() == today.getMonthValue()
                && date.getDayOfMonth() == today.getDayOfMonth();
    }

    public void onItemSaved(TreeItem<Human> savedItem) {
        ObservableList<TreeItem<Human>> children = treeTableView.getRoot().getChildren();
        dao.storeIntoToDb(savedItem.getValue());
        if (!children.contains(savedItem)) {
            children.add(savedItem);
        }
        treeTableView.sort();
    }
}
