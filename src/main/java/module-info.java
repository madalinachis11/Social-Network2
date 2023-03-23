module com.example.lab06_gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.testng;


    opens com.example.lab06_gui to javafx.fxml;
    exports com.example.lab06_gui;
    exports com.example.lab06_gui.controller;
    exports com.example.lab06_gui.domain;
    exports com.example.lab06_gui.repository;
    exports com.example.lab06_gui.service;
    exports com.example.lab06_gui.utils.events;
    exports com.example.lab06_gui.utils.observer;
    opens com.example.lab06_gui.controller to javafx.fxml;
}