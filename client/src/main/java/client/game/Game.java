package client.game;

public interface Game {
    void instantiateCommonLobby();

    void startSingleplayer();

    void joinPublicLobby();

    void initiateMultiplayerGame();

    void startMultiplayerGame();
}
