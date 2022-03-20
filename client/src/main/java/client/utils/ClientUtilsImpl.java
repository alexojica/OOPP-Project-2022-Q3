package client.utils;

import client.data.ClientData;
import client.scenes.MainCtrl;
import client.scenes.menus.WaitingCtrl;
import client.scenes.questions.EstimationQuestionCtrl;
import client.scenes.questions.GameMCQCtrl;
import commons.Player;
import commons.Question;
import commons.WebsocketMessage;
import constants.QuestionTypes;
import constants.ResponseCodes;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ClientUtilsImpl implements ClientUtils {

    //@Inject
    private ServerUtils server;

    //@Inject
    private MainCtrl mainCtrl;

    private ClientData clientData;

    public Object getCurrentSceneCtrl() {
        return currentSceneCtrl;
    }

    public void setCurrentSceneCtrl(Object currentSceneCtrl) {
        this.currentSceneCtrl = currentSceneCtrl;
    }

    private Object currentSceneCtrl;

    @Inject
    public ClientUtilsImpl(ClientData clientData, ServerUtils server, MainCtrl mainCtrl) {
        this.clientData = clientData;
        this.server = server;
        this.mainCtrl = mainCtrl;
        System.out.println("Instance of client utils");

        server.registerForMessages("/topic/nextQuestion", a -> {
            System.out.println("next question received" + clientData.getQuestionCounter());
            clientData.setQuestion(a.getQuestion());
            clientData.setPointer(a.getQuestion().getPointer());
            if(currentSceneCtrl.getClass() == WaitingCtrl.class) {
                ((WaitingCtrl) currentSceneCtrl).initiateGame();
            }
            //clientData.setQuestionCounter(clientData.getQuestionCounter() + 1);
        });

        server.registerForMessages("/topic/updateLobby", a -> {
            System.out.println(a.getCode());
            if(a.getLobbyToken().equals(clientData.getClientLobby().token)){
                clientData.setLobby(server.getLobbyByToken(a.getLobbyToken()));
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
    public void leaveLobby() {
        //Lobby currentLobby = clientData.getClientLobby();
        Player clientPlayer = clientData.getClientPlayer();

        server.send("/app/leaveLobby", new WebsocketMessage(ResponseCodes.LEAVE_LOBBY,
                clientData.getClientLobby().getToken(), clientData.getClientPlayer()));

        //set client lobby to exited
        clientData.setLobby(null);

        mainCtrl.showGameModeSelection();
    }

    @Override
    public void startTimer(ProgressBar pb, Object me, QuestionTypes questionType)
    {
        AtomicInteger r= new AtomicInteger();
        AtomicInteger g= new AtomicInteger();

        Timer timer = new Timer();
        AtomicBoolean ok = new AtomicBoolean(false);
        AtomicReference<Double> progress = new AtomicReference<>((double) 1);
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
                    if(pb.getProgress() <= 0)
                    {
                        timer.cancel();
                        if(!ok.get()) {
                            if(questionType == QuestionTypes.MULTIPLE_CHOICE_QUESTION){
                                ((GameMCQCtrl) me).nextQuestion();
                            }else if(questionType == QuestionTypes.ESTIMATION_QUESTION){
                                ((EstimationQuestionCtrl) me).nextQuestion();
                            }
                            //getQuestion(server,mainCtrl);
                            ok.set(true);
                        }
                    }
                });
            }
        },0,200);
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
                mainCtrl.showGameMCQ(); // this is only a placeholder, should be changed later
                break;
            default: break;
        }
    }
}
