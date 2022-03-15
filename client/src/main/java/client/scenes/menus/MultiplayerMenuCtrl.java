package client.scenes.menus;

import client.data.ClientData;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Lobby;
import commons.Player;
import constants.ConnectionStatusCodes;

import javax.inject.Inject;

public class MultiplayerMenuCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final ClientData clientData;

    @Inject
    public MultiplayerMenuCtrl(ServerUtils server, MainCtrl mainCtrl, ClientData clientData) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.clientData = clientData;
    }

    public void back(){
        mainCtrl.showGameModeSelection();
    }

    public void joinPublicLobby(){

        Player clientPlayer = clientData.getClientPlayer();

        ConnectionStatusCodes permissionCode = server.getConnectPermission("COMMON", clientPlayer.name);

        switch(permissionCode){
            case USERNAME_ALREADY_USED:
                mainCtrl.showPopUp("public");
                break;
            case LOBBY_NOT_FOUND:
                //lobby not found
                break;
            case CONNECTION_PERMISSION_GRANTED:
                Lobby commonLobby = server.getLobbyByToken("COMMON");
                //set client lobby static variable
                clientData.setLobby(commonLobby);

                //adds player to lobby (client sided)
                commonLobby.addPlayerToLobby(clientPlayer);

                //save the new state of the lobby to the repository again
                server.addLobby(commonLobby);

                mainCtrl.showWaiting();
        }

    }


}
