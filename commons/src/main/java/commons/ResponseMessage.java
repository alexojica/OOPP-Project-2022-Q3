package commons;

import constants.ResponseCodes;

public class ResponseMessage {

    public ResponseCodes getCode() {
        return code;
    }

    public String getLobbyToken() {
        return lobbyToken;
    }

    private ResponseCodes code;

    private String lobbyToken;

    private Question question;

    private Long pointer;

    public Long getPointer() {
        return pointer;
    }

    public Question getQuestion() {
        return question;
    }
//private Lobby lobby;

//    public Lobby getLobby() {
//        return lobby;
//    }

    /**
     * Constructor for start game
     * @param code
     * @param lobbyToken
     */
    public ResponseMessage(ResponseCodes code, String lobbyToken/*, Optional<Lobby> lobby*/){
        this.code = code;
        //this.lobby = null;
        this.lobbyToken = lobbyToken;
    }

    public ResponseMessage(ResponseCodes code, String lobbyToken, Question question){
        this.code = code;
        this.question = question;
        this.lobbyToken = lobbyToken;
    }

    public ResponseMessage(ResponseCodes code, String lobbyToken, Long pointer){
        this.code = code;
        this.pointer = pointer;
        this.lobbyToken = lobbyToken;
    }

    public ResponseMessage(){

    }

//    public ResponseMessage(ResponseCodes code, String lobbyToken, Lobby lobby){
//        this.code = code;
//        this.lobbyToken = lobbyToken;
//        this.lobby = lobby;
//    }
}
