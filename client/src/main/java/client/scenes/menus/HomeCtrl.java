package client.scenes.menus;

import client.data.ClientData;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;

import javax.inject.Inject;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import commons.Player;
import javafx.stage.Modality;
import commons.Lobby;

import java.util.List;

public class HomeCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final ClientData clientData;

    @FXML
    private TextField name;

    @Inject
    public HomeCtrl(ServerUtils server, MainCtrl mainCtrl, ClientData clientData) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.clientData = clientData;
    }

    public void play(){

        try
        {
            Player p = getPlayer();

            Player serverPlayer = server.addPlayer(p);

            //store client player info received from the server
            clientData.setPlayer(serverPlayer);
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

    //these methods are called onLoad automatically

    public void onLoad()
    {
        setRandomInitName();
        instantiateCommonLobby();
    }

    public void setRandomInitName()
    {
        //this string should be randomly generated
        //from a pool of possible name combinations
        // ex: MonkeyEye64, KingTower12 etc

        this.name.setText("testPlayer");
    }

    public void instantiateCommonLobby()
    {
        //this code should be private static string final somewhere
        String commonCode = "COMMON";

        List<Lobby> lobbies = server.getAllLobbies();

        if(lobbies.size() == 0)
        {
            //no lobbies instantiated

            Lobby mainLobby = new Lobby(commonCode);
            server.addLobby(mainLobby);
            System.out.println("Lobby created");
        }
        else
        {
            //lobbies exist, but there might not be any common lobby
            //TASK: improve the search of lobbies; maybe server sided, not client sided

            boolean commonLobbyExists = false;
            for(Lobby l : lobbies)
            {
                if(l.getToken().equals(commonCode))
                    commonLobbyExists = true;
            }

            if(!commonLobbyExists)
            {
                Lobby mainLobby = new Lobby(commonCode);
                server.addLobby(mainLobby);
            }
        }
    }

}
