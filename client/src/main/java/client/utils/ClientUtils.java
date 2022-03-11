package client.utils;

import client.ClientData;
import client.scenes.MainCtrl;
import commons.Lobby;
import commons.Player;
import commons.Question;

public class ClientUtils {


    
    public static void leaveLobby(ServerUtils server, MainCtrl mainCtrl){
        Lobby currentLobbby = ClientData.getClientLobby();
        Player clientPlayer = ClientData.getClientPlayer();

        //set client lobby to exited
        ClientData.setLobby(null);

        //removes player from lobby (client sided)
        currentLobbby.removePlayerFromLobby(clientPlayer);

        //save the new state of the lobby to the repository again
        server.addLobby(currentLobbby);

        mainCtrl.showGameModeSelection();
    }

    public static void getQuestion(ServerUtils server, MainCtrl mainCtrl){
        System.out.println("Pointer:" + ClientData.getClientPointer() + "Token:" + ClientData.getClientLobby().getToken());
        
        ClientData.setQuestion(server.getQuestion(ClientData.getClientPointer(), ClientData.getClientLobby().getToken()));

        ClientData.setPointer(ClientData.getClientQuestion().getPointer());

        System.out.println("Type:" + ClientData.getClientQuestion().getType());

        switch(ClientData.getClientQuestion().getType())
        {
            case 0:{
                    mainCtrl.showGameMCQ();
                break;
            }
            case 1:{
                    mainCtrl.showGameEstimation();
                break;
            }
            case 2:{

                break;
            }
            default: break;
        }
    }
}
