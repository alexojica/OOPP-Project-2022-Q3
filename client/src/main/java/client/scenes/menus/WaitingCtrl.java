package client.scenes.menus;

import client.data.ClientData;
import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Lobby;
import commons.Player;
import javafx.application.Platform;
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

public class WaitingCtrl implements Initializable{

    private final ServerUtils server;
    private final ClientUtils client;
    private final ClientData clientData;

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

    private Timer timer;

    @Inject
    public WaitingCtrl(ServerUtils server, MainCtrl mainCtrl, ClientUtils client, ClientData clientData) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.client = client;
        this.clientData = clientData;
    }

    /**
     * Method that sets up how the scene should look like when switched to
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
        activePlayers = clientData.getClientLobby().getPlayersInLobby();

        refresh();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        usernameColumn.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().name));
        //also initialization done for the avatar path/ logo directly

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        }, 0, 250);
    }

    public boolean isInLobby()
    {
        if(clientData.getClientLobby() == null) return false;
        return true;
    }

    public void refresh()
    {
        if(activePlayers != null && isInLobby())
        {
            String token = clientData.getClientLobby().getToken();
            Lobby current = server.getLobbyByToken(token);
            clientData.setLobby(current);
            activePlayers = current.getPlayersInLobby();
            playerData = FXCollections.observableList(activePlayers);
            tableView.setItems(playerData);

            //check if game has started in this lobby
            if(current.getStarted()) {
                timer.cancel();
                Platform.runLater(() -> initiateGame());
                //initiateGame();
                System.out.println("Game started in lobby " + token);
            }
        }
    }

    public void leaveLobby(){
        client.leaveLobby();
    }

    public void initiateGame()
    {
        clientData.setPointer(clientData.getClientLobby().getPlayerIds().get(0));
        clientData.setClientScore(0L);
        clientData.setQuestionCounter(0);

        //add delay until game starts
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //TODO: add timer progress bar / UI text with counter depleting until the start of the game
                    Thread.sleep(3000);

                    //prepare the question again only if not host
                    if(!clientData.getIsHost()) client.prepareQuestion();
                    Platform.runLater(() -> client.getQuestion());

                }catch (InterruptedException e){
                    e.printStackTrace();
                    System.out.println("Something went wrong while waiting to start the game");
                }
            }
        });
        thread.start();
    }

    //Only one player presses start game
    //that player now becomes the HOST
    //the HOST precalculates the question - only one api call
    //the rest use the pregenerated question

    public void startGame(){
        //start the game for the other players as well
        String token = clientData.getClientLobby().getToken();
        server.startLobby(token);

        clientData.setPointer(clientData.getClientLobby().getPlayerIds().get(0));
        clientData.setClientScore(0L);
        clientData.setQuestionCounter(0);
        clientData.setAsHost(true);

        client.prepareQuestion();
    }
}
