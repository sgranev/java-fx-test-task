package com.example.humancreator.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class ItemEditEvent extends Event {
    public static final EventType<ItemEditEvent> ITEM_SAVED = new EventType<>(ANY, "ITEM_SAVED");

    public ItemEditEvent() {
        this(ITEM_SAVED);
    }

    public ItemEditEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public ItemEditEvent(Object source, EventTarget target, EventType<? extends Event> eventType) {
        super(source, target, eventType);
    }
}
