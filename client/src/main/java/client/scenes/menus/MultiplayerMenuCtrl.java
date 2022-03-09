package client.scenes.menus;

import client.ClientData;
import client.scenes.MainCtrl;
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

        Player clientPlayer = ClientData.getClientPlayer();

        int permissionCode = server.getConnectPermission("COMMON", clientPlayer.name);

        switch(permissionCode){
            case 0:
                mainCtrl.showPopUp("public");
                break;
            case 1:
                //lobby not found
                break;
            case 2:
                Lobby commonLobby = server.getLobbyByToken("COMMON");
                //set client lobby static variable
                ClientData.setLobby(commonLobby);

                //adds player to lobby (client sided)
                commonLobby.addPlayerToLobby(clientPlayer);

                //save the new state of the lobby to the repository again
                server.addLobby(commonLobby);

                mainCtrl.showWaiting();
        }

    }


}
