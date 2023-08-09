package com.example.humancreator.dto;

import javafx.beans.property.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Human {
    private long id;
    private final StringProperty name = new SimpleStringProperty();
    private final IntegerProperty age = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> birthday = new SimpleObjectProperty<>();

    public Human(long id, String name, int age, LocalDate birthday) {
        this.id = id;
        this.name.set(name);
        this.age.set(age);
        this.birthday.set(birthday);
    }

    public Human() {
        this.id = -1;
    }

    public void setId(long id) {
        this.id = id;
    }
}
