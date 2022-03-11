package client.utils;

import client.ClientData;
import client.scenes.MainCtrl;
import client.scenes.questions.EstimationQuestionCtrl;
import client.scenes.questions.GameMCQCtrl;
import commons.Lobby;
import commons.Player;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ClientUtils {

    public static void leaveLobby(ServerUtils server, MainCtrl mainCtrl){
        Lobby currentLobbby = ClientData.getClientLobby();
        Player clientPlayer = ClientData.getClientPlayer();

        //set client lobby to exited
        ClientData.setLobby(null);

        //removes player from lobby (client sided)
        currentLobbby.removePlayerFromLobby(clientPlayer);

        //save the new state of the lobby to the repository again
        server.addLobby(currentLobbby);

        mainCtrl.showGameModeSelection();
    }

    public static void startTimer(ProgressBar pb,ServerUtils server, MainCtrl mainCtrl, Object me, int type)
    {
        pb.setProgress(0);
        Timer timer = new Timer();
        AtomicBoolean ok = new AtomicBoolean(false);
        AtomicReference<Double> progress = new AtomicReference<>((double) 0);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    progress.updateAndGet(v -> new Double((double) (v + 0.01)));
                    pb.setProgress(progress.get());
                    System.out.println(pb.getProgress());
                    if(pb.getProgress() >= 0.999)
                    {
                        timer.cancel();
                        if(!ok.get()) {
                            if(type == 0) ((GameMCQCtrl) me).nextQuestion();
                            if(type == 1) ((EstimationQuestionCtrl) me).nextQuestion();
                            //getQuestion(server,mainCtrl);
                            ok.set(true);
                        }
                    }
                });
            }
        },0,200);
    }

    public static void getQuestion(ServerUtils server, MainCtrl mainCtrl){
        
        ClientData.setQuestion(server.getQuestion(ClientData.getClientPointer(), ClientData.getClientLobby().getToken()));

        ClientData.setPointer(ClientData.getClientQuestion().getPointer());

        System.out.println("Pointer:" + ClientData.getClientPointer() + "Token:" + ClientData.getClientLobby().getToken());

        System.out.println("Type:" + ClientData.getClientQuestion().getType());

        switch(ClientData.getClientQuestion().getType())
        {
            case 0:
                mainCtrl.showGameMCQ();
                break;

            case 1:
                mainCtrl.showGameEstimation();
                break;

            case 2:
                break;
            default: break;
        }
    }
}
