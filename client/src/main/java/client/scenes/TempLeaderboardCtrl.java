package client.scenes;

import client.Main;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class TempLeaderboardCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public TempLeaderboardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void leaveGame() {
        mainCtrl.showOverview(); //should go to splash screen
    }

}
