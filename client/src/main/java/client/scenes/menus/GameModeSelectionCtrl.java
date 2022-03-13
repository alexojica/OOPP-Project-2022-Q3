package client.scenes.menus;

import client.data.ClientData;
import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Lobby;

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

        clientData.setLobby(new Lobby("SINGLE_PLAYER"));
        clientData.setPointer(0L);
        clientData.setClientScore(0L);

        client.getQuestion();
        mainCtrl.showGameMCQ();
    }

    public void multiplayer(){
        mainCtrl.showMultiplayerMenu();
    }
}
