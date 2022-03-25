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

    private String newToken;

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

    public String getNewToken() {
        return newToken;
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
     * Constructor used for a message that represents the start game.
     * @param startGame
     * @param token
     */
    public WebsocketMessage(ResponseCodes startGame, String token) {
        this.code = startGame;
        this.lobbyToken = token;
    }

    /**
     * Constructor used for sending messages when a joker is used.
     * @param jokerType
     * @param lobbyToken
     */
    public WebsocketMessage(JokerType jokerType, String lobbyToken) {
        this.jokerType = jokerType;
        this.lobbyToken = lobbyToken;
    }


    /**
     * The variety of constructors present here facilitates
     * the creation of websocket messages for every specific use case
     * using only the parameters needed.
     */

    /**
     * Constructor for start game message.
     * The clients will receive this message
     * and update the lobby through rest using the new token
     * @param responseCodes represents the type of message that is transmitted
     * @param lobbyToken represents the old lobby token so the clients will know the message is for them
     * @param newToken represents the new lobby token that will be used to fetch the new lobby through rest.
     */
    public WebsocketMessage(ResponseCodes responseCodes, String lobbyToken, String newToken) {
        this.code = responseCodes;
        this.lobbyToken = lobbyToken;
        this.newToken = newToken;
    }

    /**
     * Constructor for messages that carry question information
     * as a response to the /app/nextQuestion message from the client
     * @param code represents the type of message that is sent
     * @param lobbyToken represents the token for the lobby that the message is meant to
     * @param question represents the question transmitted
     */
    public WebsocketMessage(ResponseCodes code, String lobbyToken, Question question){
        this.code = code;
        this.question = question;
        this.lobbyToken = lobbyToken;
    }

    /**
     * Constructor for a message that requests a new question.
     * The message is sent from the client to the server.
     * @param code represents the type of message
     * @param lobbyToken represents the target lobby for the response message
     * @param pointer represents the pointer used in generating the question.
     */
    public WebsocketMessage(ResponseCodes code, String lobbyToken, Long pointer){
        this.code = code;
        this.pointer = pointer;
        this.lobbyToken = lobbyToken;
    }

    /**
     * Used to send a message to the server when a
     * player leaves the lobby or when the score is updated
     * @param code
     * @param lobbyToken
     * @param player
     */
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
