package com.example.humancreator.controller;

import com.example.humancreator.dto.Human;
import com.example.humancreator.event.ItemEditEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.text.ParseException;
import java.time.LocalDate;

import static com.example.humancreator.Application.DATE_FORMATTER;

public class EditFormController {
    @FXML
    private TextField name;
    @FXML
    private TextField age;
    @FXML
    private TextField birthday;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;

    private Human humanToEdit;


    @FXML
    void saveButtonAction() throws ParseException {
        mapToHuman();
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.fireEvent(new ItemEditEvent());
        stage.close();
    }

    @FXML
    private void cancelButtonAction(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void mapToTextFields(Human human) {
        if (human.getId() > 0) {
            name.setText(human.getName().getValue());
            age.setText(String.valueOf(human.getAge().getValue()));
            birthday.setText(human.getBirthday().getValue().format(DATE_FORMATTER));
        }
    }

    public void setHumanToEdit(Human humanToEdit) {
        this.humanToEdit = humanToEdit;
        mapToTextFields(humanToEdit);
    }

    private void mapToHuman() throws ParseException {
        humanToEdit.getName().set(name.getText());
        humanToEdit.getAge().set(Integer.parseInt(age.getText()));
        humanToEdit.getBirthday().set(LocalDate.parse(birthday.getText()));
    }

}