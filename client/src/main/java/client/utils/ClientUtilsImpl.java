package client.utils;

import client.ClientData;
import client.scenes.MainCtrl;
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
import java.util.concurrent.atomic.AtomicReference;

public class ClientUtilsImpl implements ClientUtils {

    @Inject
    private ServerUtils server;

    @Inject
    private MainCtrl mainCtrl;
    
    @Inject 
    private ClientData clientData;

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
        pb.setProgress(0);
        Timer timer = new Timer();
        AtomicBoolean ok = new AtomicBoolean(false);
        AtomicReference<Double> progress = new AtomicReference<>((double) 0);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    progress.updateAndGet(v -> (double) (v + 0.01));
                    pb.setProgress(progress.get());
//                    System.out.println(pb.getProgress());
                    if(pb.getProgress() >= 0.999)
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
    public void getQuestion(){

        Question foundQuestion = server.getQuestion(
                clientData.getClientPointer(), clientData.getClientLobby().getToken());

        clientData.setQuestion(foundQuestion);

        clientData.setPointer(foundQuestion.getPointer());

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
                mainCtrl.showGameMCQ(); // this is only a placeholder, should be changed later
                break;
            default: break;
        }
    }
}
