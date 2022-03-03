package client;

import commons.Lobby;
import commons.Player;

public class ClientData {

    /**
     * This class holds client data about the player that should remain accesible from the entire client
     * It will also be more useful later on in case we have to do de/serialization, since this holds all player info
     * Might also come in handy for cookie parsing, as this info should retain
     */

    private static Player clientPlayer;
    private static Lobby clientLobby;

    public static void setPlayer(Player player)
    {
        clientPlayer = player;
    }

    public static Player getClientPlayer(){return clientPlayer;}

    public static void setLobby(Lobby lobby)
    {
        clientLobby = lobby;
    }

    public static Lobby getClientLobby(){return clientLobby;}
}
