package client.scenes.menus;

import client.ClientData;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Lobby;
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
import java.util.Timer;
import java.util.TimerTask;

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

    private List<Player> activePlayers;

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

    /**
     * Method that shows active players in a given lobby
     * The lobby field from CliendData should have been filled/updated prior to calling this method
     */

    public void showActivePlayers()
    {
        activePlayers = ClientData.getClientLobby().getPlayersInLobby();

        refresh();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        usernameColumn.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().name));
        //also initialization done for the avatar path/ logo directly

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        }, 0, 500);
    }

    public boolean isInLobby()
    {
        if(ClientData.getClientLobby() == null) return false;
        return true;
    }

    public void refresh()
    {
        if(activePlayers != null && isInLobby())
        {
            String token = ClientData.getClientLobby().getToken();
            Lobby current = server.getLobbyByToken(token);
            ClientData.setLobby(current);
            activePlayers = current.getPlayersInLobby();
            playerData = FXCollections.observableList(activePlayers);
            tableView.setItems(playerData);
        }
    }

    public void leaveLobby(){
        Lobby currentLobbby = ClientData.getClientLobby();
        Player clientPlayer = ClientData.getClientPlayer();

        //set client lobby to exited
        ClientData.setLobby(null);

        //removes player from lobby (client sided)
        currentLobbby.removePlayerFromLobby(clientPlayer);

        //save the new state of the lobby to the repository again
        server.addLobby(currentLobbby);

        mainCtrl.showGameModeSelection();
    }

    public void startGame(){
        mainCtrl.showGameMCQ();
    }


}
