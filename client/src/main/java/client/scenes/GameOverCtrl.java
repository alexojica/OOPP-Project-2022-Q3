package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class GameOverCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public GameOverCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void playAgain() {

    }

    public void leaveGame() {

    }
}
