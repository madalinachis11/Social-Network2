package com.example.lab06_gui;

import com.example.lab06_gui.controller.LoginController;
import com.example.lab06_gui.controller.UtilizatorController;
import com.example.lab06_gui.domain.Prietenie;
import com.example.lab06_gui.domain.Utilizator;
import com.example.lab06_gui.domain.validators.PrietenieValidator;
import com.example.lab06_gui.domain.validators.UtilizatorValidator;
import com.example.lab06_gui.repository.Repository;
import com.example.lab06_gui.repository.db.PrietenieDbRepository;
import com.example.lab06_gui.repository.db.UtilizatorDbRepository;
import com.example.lab06_gui.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {

    Repository<Long, Utilizator> utilizatorRepository;
    Repository<String, Prietenie> prietenieRepository;
    Service service;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        System.out.println("Reading data from file");
        String username="postgres";
        String pasword="m20022002";
        String url="jdbc:postgresql://localhost:5432/socialnetwork2";
        utilizatorRepository = new UtilizatorDbRepository(url, username, pasword, new UtilizatorValidator());
        prietenieRepository = new PrietenieDbRepository(url, username, pasword, new PrietenieValidator());

        service = new Service(utilizatorRepository, prietenieRepository);
//        initView(primaryStage);
        loginView(primaryStage);
        primaryStage.show();
    }

    private void loginView(Stage stage) throws IOException {

        URL fxmlLocation = HelloApplication.class.getResource("views/Login.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        GridPane Layout = fxmlLoader.load();
        stage.setScene(new Scene(Layout));
        stage.setTitle("Login Page");

        LoginController userController = fxmlLoader.getController();
        userController.setUtilizatorService(service);
    }

    private void initView(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/UtilizatorView.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        UtilizatorController userController = fxmlLoader.getController();
        userController.setUtilizatorService(service);
    }
}
