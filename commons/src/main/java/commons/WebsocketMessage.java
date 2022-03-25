package commons;

import constants.JokerType;
import constants.ResponseCodes;

import java.util.Objects;

public class WebsocketMessage {

    private ResponseCodes code;

    private String lobbyToken;

    private String message;

    private Question question;

    private Long pointer;

    private Player player;

    private JokerType jokerType;

    private Boolean isPlayerHost;

    public JokerType getJokerType() {
        return jokerType;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Long getPointer() {
        return pointer;
    }

    public Question getQuestion() {
        return question;
    }

    public String getMessage() {
        return message;
    }
    public Boolean getIsPlayerHost() {
        return isPlayerHost;
    }

    public ResponseCodes getCode() {
        return code;
    }

    public String getLobbyToken() {
        return lobbyToken;
    }

    /**
     * Constructor for start game
     * @param code
     * @param lobbyToken
     */
    public WebsocketMessage(ResponseCodes code, String lobbyToken/*, Optional<Lobby> lobby*/){
        this.code = code;
        //this.lobby = null;
        this.lobbyToken = lobbyToken;
    }

    public WebsocketMessage(JokerType jokerType, String lobbyToken) {
        this.jokerType = jokerType;
        this.lobbyToken = lobbyToken;
    }

    public WebsocketMessage(ResponseCodes code, String lobbyToken, Question question){
        this.code = code;
        this.question = question;
        this.lobbyToken = lobbyToken;
    }

    public WebsocketMessage(ResponseCodes code, String lobbyToken, Long pointer){
        this.code = code;
        this.pointer = pointer;
        this.lobbyToken = lobbyToken;
    }

    public WebsocketMessage(ResponseCodes code, String lobbyToken, Player player){
        this.code = code;
        this.player = player;
        this.lobbyToken = lobbyToken;
    }

    public WebsocketMessage(ResponseCodes code, String lobbyToken, Player player, Boolean isPlayerHost){
        this.code = code;
        this.player = player;
        this.lobbyToken = lobbyToken;
        this.isPlayerHost = isPlayerHost;
    }

    /**
     * Constructor for sending messages, such as emotes or disconnect notifications,
     * to players.
     * @param message
     * @param lobbyToken
     */
    public WebsocketMessage(String message, String lobbyToken){
        this.message = message;
        this.lobbyToken = lobbyToken;
    }

    public WebsocketMessage(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebsocketMessage that = (WebsocketMessage) o;
        return code == that.code &&Objects.equals(lobbyToken, that.lobbyToken) && Objects.equals(message, that.message)
                && Objects.equals(question, that.question) && Objects.equals(pointer, that.pointer) &&
                Objects.equals(player, that.player) && jokerType == that.jokerType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, lobbyToken, message, question, pointer, player, jokerType);
    }

//    public ResponseMessage(ResponseCodes code, String lobbyToken, Lobby lobby){
//        this.code = code;
//        this.lobbyToken = lobbyToken;
//        this.lobby = lobby;
//    }
}
