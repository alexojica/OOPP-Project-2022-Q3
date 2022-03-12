package client.scenes.menus;

import client.ClientData;
import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Lobby;

import javax.inject.Inject;

public class GameModeSelectionCtrl {

    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;

    @Inject
    public GameModeSelectionCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.client = client;
    }

    public void back(){
        mainCtrl.showHome();
    }

    public void singleplayer(){

        ClientData.setLobby(new Lobby("SINGLE_PLAYER"));
        ClientData.setPointer(0L);
        ClientData.setClientScore(0L);

        client.getQuestion();
        mainCtrl.showGameMCQ();
    }

    public void multiplayer(){
        mainCtrl.showMultiplayerMenu();
    }
}
