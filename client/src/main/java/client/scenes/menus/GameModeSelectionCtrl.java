package client.scenes.menus;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;

import javax.inject.Inject;

public class GameModeSelectionCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public GameModeSelectionCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void back(){
        mainCtrl.showHome();
    }

    public void singleplayer(){
        mainCtrl.showGameMCQ();
    }

    public void multiplayer(){
        mainCtrl.showMultiplayerMenu();
    }

    public void leaderboard(){
        mainCtrl.showLeaderboard();
    }
}
