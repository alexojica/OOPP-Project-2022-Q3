package client.scenes.leaderboards;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;

import javax.inject.Inject;

public class LeaderboardCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public LeaderboardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void back(){
        mainCtrl.showGameModeSelection();
    }
}
