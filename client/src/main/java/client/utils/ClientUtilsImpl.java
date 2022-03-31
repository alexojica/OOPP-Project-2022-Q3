package client.utils;

import client.data.ClientData;
import client.game.Game;
import client.scenes.MainCtrl;
import client.scenes.menus.WaitingCtrl;
import client.scenes.questions.EnergyAlternativeQuestionCtrl;
import client.scenes.questions.EstimationQuestionCtrl;
import client.scenes.questions.GameMCQCtrl;
import client.scenes.questions.GuessConsumptionCtrl;
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

    private StompSession.Subscription nextQuestionSubscription, updateLobbySubscription, messageSubscription;

    AtomicReference<Double> progress;

    private boolean isSubscribed;

    public Object getCurrentSceneCtrl() {
        return currentSceneCtrl;
    }

    public void setCurrentSceneCtrl(Object currentSceneCtrl) {
        this.currentSceneCtrl = currentSceneCtrl;
    }


    AtomicReference<Double> timeLeft;

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
    }


    public void registerLobbyCommunication()
    {
        if(updateLobbySubscription == null) {
            updateLobbySubscription = server.registerForMessages("/topic/updateLobby", a -> {
                if (a.getLobbyToken().equals(clientData.getClientLobby().getToken())) {

                    System.out.println(a.getCode());

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
        isSubscribed = true;
    }

    public void registerQuestionCommunication()
    {
        if(nextQuestionSubscription == null) {
            nextQuestionSubscription = server.registerForMessages("/topic/nextQuestion", a -> {
                if(a.getLobbyToken().equals(clientData.getClientLobby().getToken())) {

                    System.out.println("next question received " + clientData.getQuestionCounter());
                    clientData.setQuestion(a.getQuestion());

                    System.out.println("Activities got are: " + a.getQuestion().getFoundActivities());
                    clientData.setPointer(a.getQuestion().getPointer());
                    if (currentSceneCtrl.getClass() == WaitingCtrl.class) {
                        game.initiateMultiplayerGame();
                    }
                }
            });
        }
        isSubscribed = true;
    }

    public void registerMessageCommunication(){
        if(messageSubscription == null){
            messageSubscription = server.registerForMessages("/topic/playerMessages", a -> {
                updateMessages(a.getMessage(), a.getLobbyToken());
            });
        }
        isSubscribed = true;
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
        timeLeft = new AtomicReference<Double>(1D);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    progress.updateAndGet(v -> (v - 0.01D));
                    timeLeft.updateAndGet(v -> (v - 0.01D));
                    pb.setProgress(progress.get());
                    r.set((int) Math.floor(255 - progress.get() * 255));
                    g.set((int) Math.floor(progress.get() * 255));
                    pb.setStyle("-fx-accent: rgb(" + r + "," + g + ", " + 0 + ");");
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
                        if(currentSceneCtrl instanceof GuessConsumptionCtrl){
                            if(((GuessConsumptionCtrl) currentSceneCtrl).getAnswer1().isSelected()){
                                coefficient = pb.getProgress();
                                updateCoefficient.set(true);
                            }
                            if(((GuessConsumptionCtrl) currentSceneCtrl).getAnswer2().isSelected()){
                                coefficient = pb.getProgress();
                                updateCoefficient.set(true);
                            }
                            if(((GuessConsumptionCtrl) currentSceneCtrl).getAnswer3().isSelected()){
                                coefficient = pb.getProgress();
                                updateCoefficient.set(true);
                            }
                        }
                    }
                    if(progress.get() <= 0){
                        switch (questionType){
                            case MULTIPLE_CHOICE_QUESTION:
                                ((GameMCQCtrl) me).disableAnswers();
                                break;
                            case ESTIMATION_QUESTION:
                                ((EstimationQuestionCtrl) me).disableSubmitButton();
                                break;
                            case ENERGY_ALTERNATIVE_QUESTION:
                                ((EnergyAlternativeQuestionCtrl) me).disableAnswers();
                                break;
                            case GUESS_X:
                                ( (GuessConsumptionCtrl) me).disableAnswers();
                                break;
                        }
                    }
                    if(timeLeft.get() <= 0)
                    {
                        timer.cancel();

                        if(!ok.get()) {
                            if(questionType == MULTIPLE_CHOICE_QUESTION){
                                ((GameMCQCtrl) me).nextQuestion();
                            }else if(questionType == QuestionTypes.ESTIMATION_QUESTION){
                                ((EstimationQuestionCtrl) me).nextQuestion();
                            }else if(questionType == QuestionTypes.ENERGY_ALTERNATIVE_QUESTION){
                                ((EnergyAlternativeQuestionCtrl) me).nextQuestion();
                            }else if(questionType == GUESS_X){
                                ((GuessConsumptionCtrl) me).nextQuestion();
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
        if(timer == null) return;
        timer.cancel();
    }

    public void halfTime(){
        System.out.println("halftime");
        progress.set(progress.get() / 2);
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
                case GUESS_X:
                    System.out.println("should appear scene");
                    mainCtrl.showGuessX();
                    break;
                default:
                    break;
            }
        }
    }

    public void unsubscribeFromMessages(){
        if(isSubscribed) {
            nextQuestionSubscription.unsubscribe();
            updateLobbySubscription.unsubscribe();
            messageSubscription.unsubscribe();

            //I have to set to null
            nextQuestionSubscription = null;
            updateLobbySubscription = null;
            messageSubscription = null;

            isSubscribed = false;
        }
    }


    /**
     * Receives a websocketmessage and updates the communication labels for all the question types, this way emotes
     * persist through different question types. Only updates the labels if the lobbytoken corresponds
     * to the one from the person who sent the method. Turning this action into a Runnable is done in order to prevent
     * an IllegalStateException.
     * @param text String received from the websocketmessage
     * @param lobbyToken token that corresponds to the lobby from the person who sent the message
     */
    public void updateMessages(String text, String lobbyToken){
        Platform.runLater(() -> {
            if(!lobbyToken.equals(clientData.getClientLobby().getToken())){
                return;
            }
            gameMCQCtrl.setMessageTxt3(gameMCQCtrl.getMessageTxt2().getText());
            gameMCQCtrl.setMessageTxt2(gameMCQCtrl.getMessageTxt1().getText());
            gameMCQCtrl.setMessageTxt1(text);

            energyAlternativeQuestionCtrl.setMessageTxt3(energyAlternativeQuestionCtrl.getMessageTxt2().getText());
            energyAlternativeQuestionCtrl.setMessageTxt2(energyAlternativeQuestionCtrl.getMessageTxt1().getText());
            energyAlternativeQuestionCtrl.setMessageTxt1(text);

            estimationQuestionCtrl.setMessageTxt3(estimationQuestionCtrl.getMessageTxt2().getText());
            estimationQuestionCtrl.setMessageTxt2(estimationQuestionCtrl.getMessageTxt1().getText());
            estimationQuestionCtrl.setMessageTxt1(text);
        });
    }

    public void resetMessages(){
        gameMCQCtrl.setMessageTxt1("");
        gameMCQCtrl.setMessageTxt2("");
        gameMCQCtrl.setMessageTxt3("");
        estimationQuestionCtrl.setMessageTxt1("");
        estimationQuestionCtrl.setMessageTxt2("");
        estimationQuestionCtrl.setMessageTxt3("");
        energyAlternativeQuestionCtrl.setMessageTxt1("");
        energyAlternativeQuestionCtrl.setMessageTxt2("");
        energyAlternativeQuestionCtrl.setMessageTxt3("");
    }

    public double getCoefficient() {
        return coefficient;
    }

}
