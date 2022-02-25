package client.scenes;

import client.utils.ServerUtils;

import javax.inject.Inject;

public class HomeCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public HomeCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void play(){
        mainCtrl.showGameModeSelection();
    }

    public void leaderboard(){
        mainCtrl.showLeaderboard();
    }
}
