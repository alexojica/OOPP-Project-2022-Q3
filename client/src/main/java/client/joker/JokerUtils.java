package client.joker;

import client.data.ClientData;
import client.scenes.questions.EnergyAlternativeQuestionCtrl;
import client.scenes.questions.EstimationQuestionCtrl;
import client.scenes.questions.GameMCQCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.WebsocketMessage;
import constants.JokerType;

import javax.inject.Inject;

/**
 * Class where the client interacts with the server through websockets
 * so every player of the same lobby gets the same affects from the jokers
 *
 * clientJoker -> joker applicable only to this specific client (ex double points)
 * lobbyJoker -> joker applicable to all players in the same lobby as this specific client, except this client
 *              (ex half available time for the others)
 */
public class JokerUtils {

    private JokerType lobbyJoker;

    private ClientData clientData;

    private ClientUtils client;

    private ServerUtils server;

    @Inject
    public JokerUtils(ClientUtils client, ServerUtils server, ClientData clientData) {
        this.client = client;
        this.server = server;
        this.clientData = clientData;
    }

    /**
     * After just joining a lobby, the client registers for joker updates
     */
    public void registerForJokerUpdates(){
        server.registerForMessages("/topic/updateJoker", msg -> {
            if(msg.getLobbyToken().equals(clientData.getClientLobby().token)){
                lobbyJoker = msg.getJokerType();
                System.out.println("received joker " + lobbyJoker);
                handleJokerCases(msg.getSenderName());
            }
        });
    }

    /**
     * Method that sends joker that was clicked on this client to server which will then distribute the message to
     * all other players of the lobby
     */
    public void sendJoker(){
        System.out.println("sending joker");
        server.send("/app/updateJoker", new WebsocketMessage(
                lobbyJoker, clientData.getClientLobby().getToken(), clientData.getClientPlayer().getName()
        ));
    }

    private void handleJokerCases(String senderName){
        switch(lobbyJoker){
            case DOUBLE_POINTS:
                doublePointsForClient();
                break;
            case ELIMINATE_ANSWERS:
                eliminateAnAnswerForClient();
                break;
            case HALF_TIME_FOR_ALL_LOBBY:
                halfTime(senderName);
                break;
        }
    }

    /**
     * Method that checks what type of controller the client is currently on and then calls doublePoints
     * which is a method all the game controllers have
     */
    private void doublePointsForClient(){
        Object curController = client.getCurrentSceneCtrl();
        if(curController instanceof GameMCQCtrl){
            ((GameMCQCtrl) curController).doublePoints();
        }else if(curController instanceof EstimationQuestionCtrl){
            ((EstimationQuestionCtrl) curController).doublePoints();
        }else if(curController instanceof EnergyAlternativeQuestionCtrl){
            ((EnergyAlternativeQuestionCtrl) curController).doublePoints();
        }
    }

    /**
     * Randomly eliminates one of the wrong answers
     * It is only applicable to the MCQ and AlternativeQuestions scenes
     */
    private void eliminateAnAnswerForClient(){
        Object curController = client.getCurrentSceneCtrl();
        if(curController instanceof GameMCQCtrl){
            ((GameMCQCtrl) curController).eliminateRandomWrongAnswer();
        }else if(curController instanceof EnergyAlternativeQuestionCtrl){
            ((EnergyAlternativeQuestionCtrl) curController).eliminateRandomWrongAnswer();
        }
    }

    /**
     * Half the time remaining as requested by another player in the lobby
     */
    public void halfTime(String senderName){
        System.out.println("checking sendername");
        if(!senderName.equals(clientData.getClientPlayer().getName()))
            client.halfTime();
    }

    public void setLobbyJoker(JokerType lobbyJoker) {
        this.lobbyJoker = lobbyJoker;
    }
}
