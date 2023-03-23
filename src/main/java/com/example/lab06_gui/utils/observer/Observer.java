package com.example.lab06_gui.utils.observer;


import com.example.lab06_gui.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}