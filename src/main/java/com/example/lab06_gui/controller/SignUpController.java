package com.example.lab06_gui.controller;

import com.example.lab06_gui.HelloApplication;
import com.example.lab06_gui.domain.Utilizator;
import com.example.lab06_gui.domain.validators.ValidationException;
import com.example.lab06_gui.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;

public class SignUpController {

    Service service;
    @FXML
    public Button SignUpButton;

    @FXML
    private Text textResponse;

    @FXML
    private TextField numeField;

    @FXML
    private TextField prenumeField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField parolaField;

    public void setUtilizatorService(Service service) {
        this.service = service;
        //service.addObserver(this);
    }

    @FXML
    public void handleAddUser(ActionEvent actionEvent) {
        //Utilizator u = new Utilizator(null, numeField.getText(), prenumeField.getText(), emailField.getText(), parolaField.getText());

        if(service.addUser(numeField.getText(), prenumeField.getText(), emailField.getText(), parolaField.getText()))
            textResponse.setText("Signed Up successfully!");

        }
}
