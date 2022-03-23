package client.game;

public interface Game {
    void instantiateCommonLobby();

    void startSingleplayer();

    void joinPublicLobby();

    void leaveLobby();

    void initiateMultiplayerGame();

    void startMultiplayerGame();
}
