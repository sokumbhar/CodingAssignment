package com.github.sokumbhar.MyApplication.model;

import java.util.Arrays;

public enum EventState {
    STARTED("STARTED"),
    FINISHED("FINISHED");

    private final String value;

    EventState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
