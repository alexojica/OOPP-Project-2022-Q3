package commons;

import constants.JokerType;
import constants.QuestionTypes;
import constants.ResponseCodes;

public class WebsocketMessage {

    public ResponseCodes getCode() {
        return code;
    }

    public String getLobbyToken() {
        return lobbyToken;
    }

    private ResponseCodes code;

    private QuestionTypes questionType;

    private String lobbyToken;

    private String message;

    private Question question;

    private Long pointer;

    private Player player;

    private JokerType jokerType;

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

    public QuestionTypes getQuestionType() {
        return questionType;
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

    /**
     * Constructor for sending messages, such as emotes or disconnect notifications,
     * to players.
     * @param questionType
     * @param message
     * @param lobbyToken
     */
    public WebsocketMessage(QuestionTypes questionType, String message, String lobbyToken){
        this.message = message;
        this.questionType = questionType;
        this.lobbyToken = lobbyToken;
    }

    public WebsocketMessage(){

    }

//    public ResponseMessage(ResponseCodes code, String lobbyToken, Lobby lobby){
//        this.code = code;
//        this.lobbyToken = lobbyToken;
//        this.lobby = lobby;
//    }
}
