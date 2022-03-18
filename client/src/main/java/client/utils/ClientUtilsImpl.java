package client.utils;

import client.data.ClientData;
import client.scenes.MainCtrl;
import client.scenes.questions.EnergyAlternativeQuestionCtrl;
import client.scenes.questions.EstimationQuestionCtrl;
import client.scenes.questions.GameMCQCtrl;
import commons.Lobby;
import commons.Player;
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

public class ClientUtilsImpl implements ClientUtils {

    @Inject
    private ServerUtils server;

    @Inject
    private MainCtrl mainCtrl;

    private final ClientData clientData;

    @Inject
    public ClientUtilsImpl(ClientData clientData) {
        this.clientData = clientData;
    }

    @Override
    public boolean isInLobby() {
        return clientData.getClientLobby() != null;
    }

    @Override
    public void leaveLobby() {
        Lobby currentLobby = clientData.getClientLobby();
        Player clientPlayer = clientData.getClientPlayer();

        //set client lobby to exited
        clientData.setLobby(null);

        //removes player from lobby (client sided)
        currentLobby.removePlayerFromLobby(clientPlayer);

        //save the new state of the lobby to the repository again
        server.addLobby(currentLobby);

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
                mainCtrl.showGameMCQ();
                break;

            case ESTIMATION_QUESTION:
                mainCtrl.showGameEstimation();
                break;

            case ENERGY_ALTERNATIVE_QUESTION:
                mainCtrl.showEnergyAlternative();
                break;
            default: break;
        }
    }
}
