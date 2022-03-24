package client.utils;

import client.data.ClientData;
import client.game.Game;
import client.scenes.MainCtrl;
import client.scenes.MessageTabCtrl;
import client.scenes.menus.WaitingCtrl;
import client.scenes.questions.EnergyAlternativeQuestionCtrl;
import client.scenes.questions.EstimationQuestionCtrl;
import client.scenes.questions.GameMCQCtrl;
import commons.Question;
import constants.QuestionTypes;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static constants.QuestionTypes.MULTIPLE_CHOICE_QUESTION;

public class ClientUtilsImpl implements ClientUtils {

    private ServerUtils server;

    private MainCtrl mainCtrl;
    private GameMCQCtrl gameMCQCtrl;
    private MessageTabCtrl messageTabCtrl;

    private ClientData clientData;

    private final Game game;

    private double coefficient;

    private Object currentSceneCtrl;

    AtomicReference<Double> progress;

    @Inject
    public ClientUtilsImpl(ClientData clientData, ServerUtils server, MainCtrl mainCtrl, MessageTabCtrl messageTabCtrl,
                           GameMCQCtrl gameMCQCtrl, Game game) {
        this.clientData = clientData;
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.game = game;
        this.gameMCQCtrl = gameMCQCtrl;
        this.messageTabCtrl = messageTabCtrl;
        System.out.println("Instance of client utils");

        server.registerForMessages("/topic/nextQuestion", a -> {
            System.out.println("next question received " + clientData.getQuestionCounter());
            clientData.setQuestion(a.getQuestion());
            clientData.setPointer(a.getQuestion().getPointer());
            if(currentSceneCtrl.getClass() == WaitingCtrl.class) {
                game.initiateMultiplayerGame();
            }
        });

        server.registerForMessages("/topic/updateLobby", a -> {
            System.out.println(a.getCode());
            if(a.getLobbyToken().equals(clientData.getClientLobby().getToken())){
                System.out.println("Before: " + clientData.getClientLobby());
                clientData.setLobby(server.getLobbyByToken(a.getLobbyToken()));
                System.out.println("After: " + clientData.getClientLobby());
                if(currentSceneCtrl.getClass() == WaitingCtrl.class)
                    ((WaitingCtrl) currentSceneCtrl).refresh();
            }
        });
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
        Timer timer = new Timer();
        AtomicBoolean ok = new AtomicBoolean(false);
        progress = new AtomicReference<>((double) 1);
        pb.setProgress(progress.get());

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
    public void getQuestion(){

        clientData.incrementQuestionCounter();

        System.out.println("[POINTER] " + clientData.getClientPointer() +
                ", [TOKEN] " + clientData.getClientLobby().getToken());

        System.out.println("[TYPE] " + clientData.getClientQuestion().getType());

        switch(clientData.getClientQuestion().getType())
        {
            case MULTIPLE_CHOICE_QUESTION:
                System.out.println("should appear scene");
                mainCtrl.showGameMCQ();
                break;

            case ESTIMATION_QUESTION:
                System.out.println("should appear scene");
                mainCtrl.showGameEstimation();
                break;

            case ENERGY_ALTERNATIVE_QUESTION:
                mainCtrl.showEnergyAlternative();
                break;
            default: break;
        }

        if(clientData.getQuestionCounter() > 20){

            mainCtrl.showGameOver();
        }

    }

    //runlater done in order to avoid an illegalstateexception
    public void updateMessages(QuestionTypes q, String text){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(q.equals(MULTIPLE_CHOICE_QUESTION)){
                    gameMCQCtrl.setMessageTxt3(gameMCQCtrl.getMessageTxt2().getText());
                    gameMCQCtrl.setMessageTxt2(gameMCQCtrl.getMessageTxt1().getText());
                    gameMCQCtrl.setMessageTxt1(text);
                    messageTabCtrl.setMessageTxt3(messageTabCtrl.getMessageTxt2().getText());
                    messageTabCtrl.setMessageTxt2(messageTabCtrl.getMessageTxt1().getText());
                    messageTabCtrl.setMessageTxt1(text);
                    //make pane reload to show changes i guess
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
