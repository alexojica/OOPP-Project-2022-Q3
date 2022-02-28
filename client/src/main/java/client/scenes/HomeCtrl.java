package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;

import javax.inject.Inject;
import java.awt.*;

import server.entities.Player;

public class HomeCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField name;

    @Inject
    public HomeCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void play(){

        server.addPlayer(getPlayer());

        mainCtrl.showGameModeSelection();
    }

    public void leaderboard(){
        mainCtrl.showLeaderboard();
    }

    private Player getPlayer()
    {
        var p = new Player(name.getText());
        return p;
    }

}
