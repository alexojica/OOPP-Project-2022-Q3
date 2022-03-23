package client.scenes.menus;

import client.game.Game;
import client.scenes.MainCtrl;

import javax.inject.Inject;

public class GameModeSelectionCtrl {

    private final MainCtrl mainCtrl;
    private final Game game;

    @Inject
    public GameModeSelectionCtrl(MainCtrl mainCtrl, Game game) {
        this.mainCtrl = mainCtrl;
        this.game = game;
    }

    public void back(){
        mainCtrl.showHome();
    }

    public void singleplayer(){
        game.startSingleplayer();
    }

    public void multiplayer(){
        mainCtrl.showMultiplayerMenu();
    }

    public void leaderboard(){
        mainCtrl.showLeaderboard();
    }
}
