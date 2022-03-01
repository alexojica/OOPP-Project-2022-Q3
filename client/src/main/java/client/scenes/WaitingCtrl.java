package client.scenes;

import client.utils.ServerUtils;
import commons.Player;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WaitingCtrl implements Initializable {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private ObservableList<Player> playerData;

    @FXML
    private TableView<Player> tableView;
    @FXML
    private TableColumn<Player, Image> avatarColumn;
    @FXML
    private TableColumn<Player, String> usernameColumn;


    @FXML
    private Text tip;

    @FXML
    private Text lobbyCode;

    @Inject
    public WaitingCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Method that sets up how the scene should look like when switched to
     * TODO: Create a class that stores a player (avatar, username...)
     * TODO: Have the 'Tip' text randomised and changed every x seconds
     */
    public void load(){
        tip.setText("Theres only one correct answer per question, get the most right to win.");
        lobbyCode.setText(lobbyCode.getText() + 59864);
        showActivePlayers();
    }

    public void showActivePlayers()
    {
        List<Player> activePlayers = server.getPlayers();

        refresh();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        usernameColumn.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().name));
        //also initialization done for the avatar path/ logo directly
    }

    public void refresh()
    {
        var players = server.getPlayers();
        playerData = FXCollections.observableList(players);
        tableView.setItems(playerData);
    }

    public void leaveLobby(){
        mainCtrl.showGameModeSelection();
    }

    public void startGame(){
        mainCtrl.showGameMCQ();
    }


}
