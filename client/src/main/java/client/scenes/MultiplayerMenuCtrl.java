package client.scenes;

import client.utils.ServerUtils;

import javax.inject.Inject;

public class MultiplayerMenuCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public MultiplayerMenuCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void back(){
        mainCtrl.showGameModeSelection();
    }

    public void joinPublicLobby(){
        mainCtrl.showWaiting();
    }


}
