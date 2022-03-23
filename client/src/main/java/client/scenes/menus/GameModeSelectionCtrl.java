package client.scenes.menus;

import client.data.ClientData;
import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Lobby;
import commons.WebsocketMessage;
import constants.ResponseCodes;
import javafx.application.Platform;

import javax.inject.Inject;

public class GameModeSelectionCtrl {

    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;
    private final ClientData clientData;

    @Inject
    public GameModeSelectionCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl, ClientData clientData) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.client = client;
        this.clientData = clientData;
    }

    public void back(){
        mainCtrl.showHome();
    }

    public void singleplayer(){


        Lobby mainLobby = new Lobby("SINGLE_PLAYER");
        server.addLobby(mainLobby);
        System.out.println("Lobby created: " + "SINGLE_PLAYER");
        clientData.setLobby(mainLobby);
        clientData.setPointer(clientData.getClientPlayer().getId());
        clientData.setClientScore(0);
        clientData.setQuestionCounter(0);
        clientData.setAsHost(true);
        server.addMeToLobby(clientData.getClientLobby().getToken(),clientData.getClientPlayer());

        //add delay until game starts
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //TODO: add timer progress bar / UI text with counter depleting until the start of the game

                    server.send("/app/nextQuestion",
                            new WebsocketMessage(ResponseCodes.NEXT_QUESTION,
                                    clientData.getClientLobby().getToken(), clientData.getClientPointer()));

                    Thread.sleep(3000);

                    Platform.runLater(() -> client.getQuestion());

                }catch (InterruptedException e){
                    e.printStackTrace();
                    System.out.println("Something went wrong while waiting to start the game");
                }
            }
        });
        thread.start();
    }

    public void multiplayer(){
        mainCtrl.showMultiplayerMenu();
    }

    public void leaderboard(){
        mainCtrl.showLeaderboard();
    }
}
