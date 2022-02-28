package client.scenes;

import client.utils.ServerUtils;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;

import javax.inject.Inject;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import commons.Player;
import javafx.stage.Modality;

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

        try
        {
            Player p = getPlayer();
            System.out.println("Hello player: " + p);

            server.addPlayer(p);

        }
        catch (WebApplicationException e)
        {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }


        mainCtrl.showGameModeSelection();
    }

    public void leaderboard(){
        mainCtrl.showLeaderboard();
    }

    private Player getPlayer()
    {
        String userName = name.getText();
        if(userName.length() == 0)
            userName = "testUserX";
        var p = new Player(userName);
        return p;
    }

}
