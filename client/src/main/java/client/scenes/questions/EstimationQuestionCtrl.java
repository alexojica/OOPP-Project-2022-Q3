package client.scenes.questions;

import client.ClientData;
import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Question;

import com.google.inject.Inject;

import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class EstimationQuestionCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private ProgressBar pb;

    @FXML
    private Text scoreTxt;

    @FXML
    private Text nQuestionsTxt;

    @FXML
    private Text questionTxt;


    @FXML
    private TextField answer;

    @Inject
    public EstimationQuestionCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void nextQuestion(){

        ClientUtils.getQuestion(server, mainCtrl);
    }

    public void load() {

        scoreTxt.setText("Score:" + ClientData.getClientScore());

        Question question = ClientData.getClientQuestion();

        ClientUtils.startTimer(pb,server,mainCtrl,this,1);

        questionTxt.setText(question.getText());    
    }

    public void submit() {

    }

    public void leaveGame() {
        ClientUtils.leaveLobby(server, mainCtrl);
    }
}
