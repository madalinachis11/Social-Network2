package com.example.lab06_gui.utils.events;

import com.example.lab06_gui.domain.Prietenie;

public class PrietenieEntityChangeEvent implements Event {
    private ChangeEventType type;
    private Prietenie oldData, data;

    public PrietenieEntityChangeEvent(ChangeEventType type, Prietenie data) {
        this.type = type;
        this.data = data;
    }

    public PrietenieEntityChangeEvent(ChangeEventType type, Prietenie oldData, Prietenie data) {
        this.type = type;
        this.oldData = oldData;
        this.data = data;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Prietenie getOldData() {
        return oldData;
    }

    public Prietenie getData() {
        return data;
    }
}
