package client;

import commons.Lobby;
import commons.Player;
import commons.Question;

public class ClientData {

    /**
     * This class holds client data about the player that should remain accesible from the entire client
     * It will also be more useful later on in case we have to do de/serialization, since this holds all player info
     * Might also come in handy for cookie parsing, as this info should retain
     */

    private static Player clientPlayer;
    private static Lobby clientLobby;
    private static Long clientPointer;
    private static Question clientQuestion;
    private static Long clientScore;

    public static Long getClientScore() {
		return clientScore;
	}

    public static void setClientScore(Long score) {
		clientScore = score;
	}

    public static void setPlayer(Player player)
    {
        clientPlayer = player;
    }

    public static Player getClientPlayer(){return clientPlayer;}

    public static void setPointer(Long pointer)
    {
        clientPointer = pointer;
    }

    public static Long getClientPointer(){return clientPointer;}

    public static void setQuestion(Question question)
    {
        clientQuestion = question;
    }

    public static Question getClientQuestion(){return clientQuestion;}

    public static void setLobby(Lobby lobby)
    {
        clientLobby = lobby;
    }

    public static Lobby getClientLobby(){return clientLobby;}
}
