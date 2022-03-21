package client.scenes.menus;

import client.data.ClientData;
import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ClientUtilsImpl;
import client.utils.ServerUtils;
import commons.Lobby;
import commons.Player;
import commons.WebsocketMessage;
import constants.ConnectionStatusCodes;
import constants.ResponseCodes;
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
        clientData.setQuestionCounter(0);

    }

    /**
     * Method that sets up how the scene should look like when switched to
     */
    public void load(){
        tip.setText("Theres only one correct answer per question, get the most right to win.");
        lobbyCode.setText(lobbyCode.getText() + 59864);
        showActivePlayers();

        if(client.getClass().equals(ClientUtilsImpl.class)) {
            ((ClientUtilsImpl) client).setCurrentSceneCtrl(this);
        }
        server.registerForMessages("/topic/lobbyStart", a -> {
            if(a.getCode() == ResponseCodes.START_GAME && a.getLobbyToken().equals(clientData.getClientLobby().token)) {
                System.out.println("ishost:" + clientData.getIsHost());
                if(clientData.getIsHost())
                    server.send("/app/nextQuestion",
                            new WebsocketMessage(ResponseCodes.NEXT_QUESTION,
                                    clientData.getClientLobby().token, clientData.getClientPointer()));
            }
        });

        server.send("/app/requestUpdate",
                new WebsocketMessage(ResponseCodes.LOBBY_UPDATED, clientData.getClientLobby().getToken()));
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
            Lobby current = clientData.getClientLobby();
            clientData.setLobby(current);
            activePlayers = current.getPlayersInLobby();
            playerData = FXCollections.observableList(activePlayers);
            tableView.setItems(playerData);

        }
    }

    public void leaveLobby(){
        client.leaveLobby();
    }

    public void initiateGame()
    {
        System.out.println("game initiated");
        clientData.setClientScore(0);


        //add delay until game starts
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //TODO: add timer progress bar / UI text with counter depleting until the start of the game
                    Thread.sleep(300);

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

        String token = clientData.getClientLobby().getToken();
        if(server.startLobby(token).equals(ConnectionStatusCodes.YOU_ARE_HOST)) {
            clientData.setAsHost(true);
            clientData.setPointer(clientData.getClientLobby().getPlayerIds().get(0));
            clientData.setClientScore(0);
            clientData.setQuestionCounter(0);

            //start the game for the other players as well
            server.send("/app/lobbyStart",
                    new WebsocketMessage(ResponseCodes.START_GAME, clientData.getClientLobby().token));
        }
    }
}
