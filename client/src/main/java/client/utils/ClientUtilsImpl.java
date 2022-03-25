package client.utils;

import client.data.ClientData;
import client.game.Game;
import client.scenes.MainCtrl;
import client.scenes.menus.WaitingCtrl;
import client.scenes.questions.EnergyAlternativeQuestionCtrl;
import client.scenes.questions.EstimationQuestionCtrl;
import client.scenes.questions.GameMCQCtrl;
import commons.Question;
import constants.QuestionTypes;
import constants.ResponseCodes;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import org.springframework.messaging.simp.stomp.StompSession;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static constants.QuestionTypes.*;

public class ClientUtilsImpl implements ClientUtils {

    private ServerUtils server;

    private MainCtrl mainCtrl;
    private GameMCQCtrl gameMCQCtrl;
    private EnergyAlternativeQuestionCtrl energyAlternativeQuestionCtrl;
    private EstimationQuestionCtrl estimationQuestionCtrl;

    private ClientData clientData;

    private final Game game;

    private double coefficient;

    private Timer timer;

    private Object currentSceneCtrl;

    private StompSession.Subscription nextQuestionSubscription, updateLobbySubscription;

    AtomicReference<Double> progress;

    public Object getCurrentSceneCtrl() {
        return currentSceneCtrl;
    }

    public void setCurrentSceneCtrl(Object currentSceneCtrl) {
        this.currentSceneCtrl = currentSceneCtrl;
    }


    @Inject
    public ClientUtilsImpl(ClientData clientData, ServerUtils server, MainCtrl mainCtrl, GameMCQCtrl gameMCQCtrl,
                           EnergyAlternativeQuestionCtrl energyAlternativeQuestionCtrl,
                           EstimationQuestionCtrl estimationQuestionCtrl, Game game) {
        this.clientData = clientData;
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.game = game;
        this.gameMCQCtrl = gameMCQCtrl;
        this.energyAlternativeQuestionCtrl = energyAlternativeQuestionCtrl;
        this.estimationQuestionCtrl = estimationQuestionCtrl;
        System.out.println("Instance of client utils");

        registerQuestionCommunication();
        registerLobbyCommunication();

    }

    public void registerLobbyCommunication()
    {
        if(updateLobbySubscription == null) {
            updateLobbySubscription = server.registerForMessages("/topic/updateLobby", a -> {
                System.out.println(a.getCode());
                if (a.getLobbyToken().equals(clientData.getClientLobby().getToken())) {

                    clientData.setLobby(server.getLobbyByToken(a.getLobbyToken()));
                    if (a.getCode() == ResponseCodes.UPDATE_HOST) {
                        if (a.getPlayer().equals(clientData.getClientPlayer())) {
                            System.out.println("New host is: " + clientData.getClientPlayer());
                            clientData.setAsHost(true);
                        }
                    }

                    if (currentSceneCtrl.getClass() == WaitingCtrl.class)
                        ((WaitingCtrl) currentSceneCtrl).refresh();
                }
            });
        }
    }

    public void registerQuestionCommunication()
    {
        if(nextQuestionSubscription == null) {
            nextQuestionSubscription = server.registerForMessages("/topic/nextQuestion", a -> {
                System.out.println("next question received " + clientData.getQuestionCounter());
                clientData.setQuestion(a.getQuestion());
                clientData.setPointer(a.getQuestion().getPointer());
                if (currentSceneCtrl.getClass() == WaitingCtrl.class) {
                    game.initiateMultiplayerGame();
                }
            });
        }
    }

    @Override
    public boolean isInLobby() {
        return clientData.getClientLobby() != null;
    }


    @Override
    public void startTimer(ProgressBar pb, Object me, QuestionTypes questionType)
    {
        AtomicInteger r= new AtomicInteger();
        AtomicInteger g= new AtomicInteger();
        AtomicBoolean updateCoefficient = new AtomicBoolean(false);
        AtomicBoolean ok = new AtomicBoolean(false);
        progress = new AtomicReference<>((double) 1);
        pb.setProgress(progress.get());

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    progress.updateAndGet(v -> (double) (v - 0.01));
                    pb.setProgress(progress.get());
                    r.set((int) Math.floor(255 - progress.get() * 255));
                    g.set((int) Math.floor(progress.get() * 255));
                    pb.setStyle("-fx-accent: rgb(" + r + "," + g + ", " + 0 + ");");
//                    System.out.println(pb.getProgress());
                    if(!updateCoefficient.get()){
                        if(currentSceneCtrl instanceof GameMCQCtrl){
                            if(((GameMCQCtrl) currentSceneCtrl).getAnswer1().isSelected()){
                                coefficient = pb.getProgress();
                                updateCoefficient.set(true);
                            }
                            if(((GameMCQCtrl) currentSceneCtrl).getAnswer2().isSelected()){
                                coefficient = pb.getProgress();
                                updateCoefficient.set(true);
                            }
                            if(((GameMCQCtrl) currentSceneCtrl).getAnswer3().isSelected()){
                                coefficient = pb.getProgress();
                                updateCoefficient.set(true);
                            }
                        }
                        if(currentSceneCtrl instanceof EnergyAlternativeQuestionCtrl){
                            if(((EnergyAlternativeQuestionCtrl) currentSceneCtrl).getAnswer1().isSelected()){
                                coefficient = pb.getProgress();
                                updateCoefficient.set(true);
                            }
                            if(((EnergyAlternativeQuestionCtrl) currentSceneCtrl).getAnswer2().isSelected()){
                                coefficient = pb.getProgress();
                                updateCoefficient.set(true);
                            }
                            if(((EnergyAlternativeQuestionCtrl) currentSceneCtrl).getAnswer3().isSelected()){
                                coefficient = pb.getProgress();
                                updateCoefficient.set(true);
                            }
                        }
                    }
                    if(pb.getProgress() <= 0)
                    {
                        timer.cancel();

                        if(!ok.get()) {
                            if(questionType == MULTIPLE_CHOICE_QUESTION){
                                ((GameMCQCtrl) me).nextQuestion();
                            }else if(questionType == QuestionTypes.ESTIMATION_QUESTION){
                                ((EstimationQuestionCtrl) me).nextQuestion();
                            }else if(questionType == QuestionTypes.ENERGY_ALTERNATIVE_QUESTION){
                                ((EnergyAlternativeQuestionCtrl) me).nextQuestion();
                            }
                            //getQuestion(server,mainCtrl);
                            ok.set(true);
                        }
                    }
                });
            }
        },0,200);
    }

    public void killTimer()
    {
        timer.cancel();
    }

    public void halfTime(){
        progress = new AtomicReference<>(progress.get() / 2);
    }

    @Override
    public void prepareQuestion()
    {
        Question foundQuestion = server.getQuestion(
                clientData.getClientPointer(), clientData.getClientLobby().getToken());

        clientData.setQuestion(foundQuestion);

        clientData.setPointer(foundQuestion.getPointer());
    }

    @Override
    public void getQuestion() {

        if (clientData.getQuestionCounter() >= game.getQuestionsToEndGame()){
            game.endGame();
        }
        else {
            clientData.incrementQuestionCounter();

            System.out.println("[POINTER] " + clientData.getClientPointer() +
                    ", [TOKEN] " + clientData.getClientLobby().getToken());

            System.out.println("[TYPE] " + clientData.getClientQuestion().getType());

            switch (clientData.getClientQuestion().getType()) {
                case MULTIPLE_CHOICE_QUESTION:
                    System.out.println("should appear scene");
                    mainCtrl.showGameMCQ();
                    break;

                case ESTIMATION_QUESTION:
                    System.out.println("should appear scene");
                    mainCtrl.showGameEstimation();
                    break;

                case ENERGY_ALTERNATIVE_QUESTION:
                    System.out.println("should appear scene");
                    mainCtrl.showEnergyAlternative();
                    break;
                default:
                    break;
            }
        }
    }

    public void unsubscribeFromMessages(){
        nextQuestionSubscription.unsubscribe();
        updateLobbySubscription.unsubscribe();

        //I have to set to null
        nextQuestionSubscription = null;
        updateLobbySubscription = null;
    }


    /**
     * Receives a websocketmessage and updates the communication labels with the given string in the given questiontype.
     * Only updates the labels if the lobbytoken corresponds to the one from the person who sent the method.
     * Turning this action into a Runnable is done in order to prevent an IllegalStateException.
     * @param q QuestionType received from the websocketmessage
     * @param text String received from the websocketmessage
     * @param lobbyToken token that corresponds to the lobby from the person who sent the message
     */
    public void updateMessages(QuestionTypes q, String text, String lobbyToken){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(!lobbyToken.equals(clientData.getClientLobby().getToken())){
                    return;
                }
                switch(q){
                    case MULTIPLE_CHOICE_QUESTION:
                        gameMCQCtrl.setMessageTxt3(gameMCQCtrl.getMessageTxt2().getText());
                        gameMCQCtrl.setMessageTxt2(gameMCQCtrl.getMessageTxt1().getText());
                        gameMCQCtrl.setMessageTxt1(text);
                        break;
                    case ENERGY_ALTERNATIVE_QUESTION:
                        energyAlternativeQuestionCtrl.setMessageTxt3(
                                energyAlternativeQuestionCtrl.getMessageTxt2().getText());
                        energyAlternativeQuestionCtrl.setMessageTxt2(
                                energyAlternativeQuestionCtrl.getMessageTxt1().getText());
                        energyAlternativeQuestionCtrl.setMessageTxt1(text);
                        break;
                    case ESTIMATION_QUESTION:
                        estimationQuestionCtrl.setMessageTxt3(estimationQuestionCtrl.getMessageTxt2().getText());
                        estimationQuestionCtrl.setMessageTxt2(estimationQuestionCtrl.getMessageTxt1().getText());
                        estimationQuestionCtrl.setMessageTxt1(text);
                        break;
                }
            }
        });
    }

    public double getCoefficient() {
        return coefficient;
    }

    public Object getCurrentSceneCtrl() {
        return currentSceneCtrl;
    }

    public void setCurrentSceneCtrl(Object currentSceneCtrl) {
        this.currentSceneCtrl = currentSceneCtrl;
    }

}
