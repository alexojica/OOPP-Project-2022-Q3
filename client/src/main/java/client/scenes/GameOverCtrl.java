package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GameOverCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public GameOverCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void playAgain() {
        mainCtrl.showOverview(); //keep username + needs to go to lobby instead of overview
    }

    public void leaveGame() {
        mainCtrl.showOverview(); //should go to splash screen
    }
}
