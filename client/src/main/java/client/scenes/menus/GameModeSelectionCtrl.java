package client.scenes.menus;

import client.game.Game;
import client.scenes.MainCtrl;
import client.utils.ClientUtils;

import javax.inject.Inject;

public class GameModeSelectionCtrl {

    private final MainCtrl mainCtrl;
    private final Game game;
    private final ClientUtils client;

    @Inject
    public GameModeSelectionCtrl(MainCtrl mainCtrl, Game game, ClientUtils client) {
        this.mainCtrl = mainCtrl;
        this.game = game;
        this.client = client;
    }

    public void load()
    {
        client.registerQuestionCommunication();
        client.registerLobbyCommunication();
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
