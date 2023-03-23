package com.example.lab06_gui.controller;
import com.example.lab06_gui.HelloApplication;
import com.example.lab06_gui.domain.Prietenie;
import com.example.lab06_gui.domain.Utilizator;
import com.example.lab06_gui.domain.validators.ValidationException;
import com.example.lab06_gui.service.Service;
import com.example.lab06_gui.utils.events.PrietenieEntityChangeEvent;
import com.example.lab06_gui.utils.events.UtilizatorEntityChangeEvent;
import com.example.lab06_gui.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UtilizatorController implements Observer<PrietenieEntityChangeEvent> {
    Service service;
    private ObservableList<Utilizator> modelFriends = FXCollections.observableArrayList();
    private ObservableList<Utilizator> modelUser = FXCollections.observableArrayList();
    @FXML
    public Button sendRequestButton;
    @FXML
    public TextField searchBar;
    @FXML
    public Button seeRequestsButton;
    @FXML
    public ImageView myAccountIcon;
    @FXML
    public Text myAccountText;

    @FXML
    TableView<Utilizator> tableView;
    @FXML
    TableColumn<Utilizator,String> tableColumnFirstName;
    @FXML
    TableColumn<Utilizator,String> tableColumnLastName;

    private Utilizator userCurent;

    @FXML
    public void setUserCurent(Utilizator userCurent) {
        this.userCurent = userCurent;
    }

    public void setUtilizatorService(Service service) {
        this.service = service;
        service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("nume"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("prenume"));
        tableView.setItems(modelUser);
        //tableView.setItems(modelFriends);
    }

    private void initModel() {
        /*Iterable<Utilizator> messages = service.getAll();
        List<Utilizator> users = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(users);*/

        modelFriends.setAll();
        modelFriends.addAll(service.getFriendsOfUserList(userCurent));

        modelUser.setAll();
        modelUser.addAll(service.getStrangersOfUserList(userCurent));

        /*List<Utilizator> allUsers = service.getListUtilizatori();

        for(Utilizator u : allUsers)
        {
            if (!modelFriends.contains(u) && !modelUser.contains(u))
                modelUser.add(u);
        }*/

    }

    @Override
    public void update(PrietenieEntityChangeEvent utilizatorEntityChangeEvent) {
        tableView.setItems(null);
        initModel();
        initialize();
    }

    //nu le folosesc
    public void handleAddUtilizator(ActionEvent actionEvent) {
        /*Utilizator user = new Utilizator();
        try{
            Utilizator saved = service.addUser(user);
        } catch (ValidationException e){
            MessageAlert.showErrorMessage(null, e.getMessage());
            return;
        }*/
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "User adaugat cu succes!");
//        if(service.addUtilizator(user) == null){
//            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "User adaugat cu succes!");
//        } else{
//            MessageAlert.showErrorMessage(null, "Failed adding user");
//        }
    }

    //nu le folosesc
    public void handleDeleteUtilizator(ActionEvent actionEvent) {
        Utilizator user=(Utilizator) tableView.getSelectionModel().getSelectedItem();
        if (user!=null) {
            //Utilizator deleted =
                    service.deleteUser(user.getID());
        }
    }

    //nu le folosesc
    public void handleUpdateUtilizator(ActionEvent actionEvent) {
    }

    public void handleSendRequest(ActionEvent actionEvent) {
        Utilizator user = tableView.getSelectionModel().getSelectedItem();
        boolean status = service.sendFriendRequest(userCurent.getID(), user.getID());
        //  PRIMUL ID ESTE CEL AL TRIMITATORULUI DE REQUEST, AL2LEA ID ESTE CEL CARE PRIMESTE REQUESTUL!!!!
        if(status)
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Cerere trimisa cu succes!");
        else
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Info", "Cerere bidirectionala!\nPrietenie adaugata!");
    }

    public void handleActivateButtonAdd(MouseEvent actionEvent)
    {
        //tableView.getSelectionModel().clearSelection();
        sendRequestButton.setDisable(false);
    }

    public void handleClickSearch(MouseEvent actionEvent)
    {
        searchBar.setText("");
    }

    public void handleSearchUsers(KeyEvent keyEvent) {
        modelUser.setAll(service.getStrangersOfUserList(userCurent));
        tableView.setItems(modelUser);
        String valoareNume = String.valueOf(searchBar.getCharacters());
        if(!valoareNume.equals(""))
        {
            List<Utilizator> strangers = service.getStrangersOfUserList(userCurent).stream().filter(x -> (x.getNume()+x.getPrenume()).contains(valoareNume)).toList();
            modelUser.setAll(strangers);
            tableView.setItems(modelUser);
        }
        sendRequestButton.setDisable(true);
    }

    public void handleOpenSeeRequests(ActionEvent actionEvent) {
        try{
            URL fxmlLocation = HelloApplication.class.getResource("views/RequestsView.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Received Requests Page");
            RequestsController requestsController= fxmlLoader.getController();
            requestsController.setUserCurent(userCurent);
            requestsController.setUtilizatorService(service);
            requestsController.setController(this);
            stage.show();
            //((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void handleOpenAccount(MouseEvent mouseEvent) {
        try{
            URL fxmlLocation = HelloApplication.class.getResource("views/MyAccountView.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("My Account");
            MyAccountController myAccountController= fxmlLoader.getController();
            myAccountController.setUserCurent(userCurent);
            myAccountController.setUtilizatorService(service);
            myAccountController.setController(this);
            myAccountController.setPreviousStage((Node)mouseEvent.getSource()); //!!!!!!!!!!
            stage.show();
            //((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }


//    public void handleDeleteMessage(ActionEvent actionEvent) {
//        MessageTask selected = (MessageTask) tableView.getSelectionModel().getSelectedItem();
//        if (selected != null) {
//            MessageTask deleted = service.deleteMessageTask(selected);
//            if (null != deleted)
//                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "Studentul a fost sters cu succes!");
//        } else MessageAlert.showErrorMessage(null, "Nu ati selectat nici un student!");
//    }
//

//
//    @FXML
//    public void handleUpdateMessage(ActionEvent ev) {
//        MessageTask selected = tableView.getSelectionModel().getSelectedItem();
//        if (selected != null) {
//            showMessageTaskEditDialog(selected);
//        } else
//            MessageAlert.showErrorMessage(null, "NU ati selectat nici un student");
//    }
//
//    @FXML
//    public void handleAddMessage(ActionEvent ev) {
//
//        showMessageTaskEditDialog(null);
//    }
//
//    public void showMessageTaskEditDialog(MessageTask messageTask) {
//        try {
//            // create a new stage for the popup dialog.
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("/views/editMessageTaskView.fxml"));
//
//            AnchorPane root = (AnchorPane) loader.load();
//
//            // Create the dialog Stage.
//            Stage dialogStage = new Stage();
//            dialogStage.setTitle("Edit Message");
//            dialogStage.initModality(Modality.WINDOW_MODAL);
//            //dialogStage.initOwner(primaryStage);
//            Scene scene = new Scene(root);
//            dialogStage.setScene(scene);
//
//            EditMessageTaskController editMessageViewController = loader.getController();
//            editMessageViewController.setService(service, dialogStage, messageTask);
//
//            dialogStage.show();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//

}
