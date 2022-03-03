package client.scenes;

import client.ClientData;
import client.utils.ServerUtils;
import commons.Lobby;
import commons.Player;

import javax.inject.Inject;

public class MultiplayerMenuCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public MultiplayerMenuCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void back(){
        mainCtrl.showGameModeSelection();
    }

    public void joinPublicLobby(){

        //sends in also common lobby token
        Lobby commonLobby = server.getLobbyByToken("COMMON");
        Player clientPlayer = ClientData.getClientPlayer();

        //set client lobby static variable
        ClientData.setLobby(commonLobby);

        //adds player to lobby (client sided)
        commonLobby.addPlayerToLobby(clientPlayer);

        //save the new state of the lobby to the repository again
        server.addLobby(commonLobby);

        mainCtrl.showWaiting();
    }


}
