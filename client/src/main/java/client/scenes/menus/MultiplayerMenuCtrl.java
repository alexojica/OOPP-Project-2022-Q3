package client.scenes.menus;

import client.data.ClientData;
import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Player;
import constants.ConnectionStatusCodes;

import javax.inject.Inject;

public class MultiplayerMenuCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final ClientData clientData;
    private final ClientUtils client;

    @Inject
    public MultiplayerMenuCtrl(ServerUtils server, MainCtrl mainCtrl, ClientData clientData, ClientUtils client) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.clientData = clientData;
        this.client = client;
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
                //set client lobby static variable
                clientData.setLobby(server.addMeToLobby("COMMON", clientPlayer));

                if(clientData.getClientLobby().playersInLobby.contains(clientPlayer))
                    mainCtrl.showWaiting();
        }

    }


}
