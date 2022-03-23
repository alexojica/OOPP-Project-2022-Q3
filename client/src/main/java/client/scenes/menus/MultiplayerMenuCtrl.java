package client.scenes.menus;

import client.game.Game;
import client.scenes.MainCtrl;

import javax.inject.Inject;

public class MultiplayerMenuCtrl {

    private final MainCtrl mainCtrl;
    private final Game game;

    @Inject
    public MultiplayerMenuCtrl(MainCtrl mainCtrl, Game game) {
        this.mainCtrl = mainCtrl;
        this.game = game;
    }

    public void back(){
        mainCtrl.showGameModeSelection();
    }

    public void joinPublicLobby(){
        game.instantiateCommonLobby();
        game.joinPublicLobby();
    }


}
