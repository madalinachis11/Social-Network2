package com.example.lab06_gui.controller;

import com.example.lab06_gui.HelloApplication;
import com.example.lab06_gui.domain.Prietenie;
import com.example.lab06_gui.domain.Utilizator;
import com.example.lab06_gui.service.Service;
import com.example.lab06_gui.utils.events.PrietenieEntityChangeEvent;
import com.example.lab06_gui.utils.events.UtilizatorEntityChangeEvent;
import com.example.lab06_gui.utils.observer.Observer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;

import static javafx.application.Application.launch;

public class MyAccountController implements Observer<PrietenieEntityChangeEvent> {

    Service service;
    private ObservableList<Utilizator> modelFriends = FXCollections.observableArrayList();
    @FXML
    public Button logoutButton;
    @FXML
    TableView<Utilizator> tableFriendsView;
    @FXML
    TableColumn<Utilizator,String> tableColumnFirstName;
    @FXML
    TableColumn<Utilizator,String> tableColumnLastName;
    @FXML
    public Text numeField;
    @FXML
    public Text prenumeField;
    private Utilizator userCurent;
    private Node previousStage;

    public void setPreviousStage(Node previousStage) {
        this.previousStage = previousStage;
    }

    private UtilizatorController controller;

    public void setController(UtilizatorController controller) {
        this.controller = controller;
    }

    public void setUserCurent(Utilizator userCurent) {
        this.userCurent = userCurent;
    }

    public void setUtilizatorService(Service service) {
        this.service = service;
        service.addObserver(this);
        numeField.setText(userCurent.getNume());
        prenumeField.setText(userCurent.getPrenume());
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("nume"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("prenume"));

        tableFriendsView.setItems(modelFriends);
    }

    private void initModel() {

        modelFriends.setAll();
        modelFriends.addAll(service.getFriendsOfUserList(userCurent));

    }

    @Override
    public void update(PrietenieEntityChangeEvent utilizatorEntityChangeEvent) {
        initModel();
        initialize();
    }

    public void handleDeleteFriend(ActionEvent actionEvent) {
        Utilizator user = tableFriendsView.getSelectionModel().getSelectedItem();
        Prietenie prietenie = new Prietenie(user.getID(), userCurent.getID(), LocalDateTime.now());
        service.deleteFriendship(prietenie.getID());
        controller.update(null);
        update(null);
    }

    public void handleLogOut(ActionEvent actionEvent) throws IOException {
        URL fxmlLocation = HelloApplication.class.getResource("views/Login.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Login Page");
        LoginController loginController = fxmlLoader.getController();
        loginController.setUtilizatorService(service);
        stage.show();
        previousStage.getScene().getWindow().hide();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }
}
