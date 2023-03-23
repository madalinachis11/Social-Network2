package com.example.lab06_gui.controller;
import com.example.lab06_gui.HelloApplication;
import com.example.lab06_gui.domain.Utilizator;
import com.example.lab06_gui.service.Service;
import com.example.lab06_gui.utils.events.PrietenieEntityChangeEvent;
import com.example.lab06_gui.utils.events.UtilizatorEntityChangeEvent;
import com.example.lab06_gui.utils.observer.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;

public class LoginController implements Observer<PrietenieEntityChangeEvent> {
    Service service;

    @FXML
    public Text signUpClickable;

    @FXML
    private Text textResponse;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    public void setUtilizatorService(Service service) {
        this.service = service;
        service.addObserver(this);
    }

    @FXML
    public void handleSubmitButtonAction(ActionEvent actionEvent) {
//        textResponse.setText("Login button was pressed!");
        Utilizator u = service.findUserEmailParola(emailField.getText(), passwordField.getText());
        if(u!=null) {
            textResponse.setText("Login succesful!");
            try{
                URL fxmlLocation = HelloApplication.class.getResource("views/UsersView.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
                Parent root = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Main Page");
                UtilizatorController userController = fxmlLoader.getController();
                userController.setUserCurent(u);
                userController.setUtilizatorService(service);
                stage.show();
                ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
            } catch(Exception e) {
                e.printStackTrace();
            }

        }
        else
            textResponse.setText("Login failed!");

    }

    @FXML
    public void handleSignUp(MouseEvent actionEvent) {
        try{
            URL fxmlLocation = HelloApplication.class.getResource("views/SignUpView.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("SignUp Page");
            SignUpController signupController = fxmlLoader.getController();
            signupController.setUtilizatorService(service);
            stage.show();
            //((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(PrietenieEntityChangeEvent utilizatorEntityChangeEvent) {

    }
    /*@Override
    public void update(UtilizatorEntityChangeEvent utilizatorEntityChangeEvent) {

    }*/
}
