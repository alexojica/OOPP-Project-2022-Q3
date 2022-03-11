package client.scenes.questions;

import client.ClientData;
import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;


import commons.Question;

import javax.inject.Inject;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameMCQCtrl {

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

    final ToggleGroup radioGroup = new ToggleGroup(); 

    @FXML
    private RadioButton answer1;
    @FXML
    private RadioButton answer2;
    @FXML
    private RadioButton answer3;

    @Inject
    public GameMCQCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void leaveGame(){
        ClientUtils.leaveLobby(server, mainCtrl);
    }

    public void load() {

        scoreTxt.setText("Score:" + ClientData.getClientScore());

        answer1.setToggleGroup(radioGroup);
        answer2.setToggleGroup(radioGroup);
        answer3.setToggleGroup(radioGroup);

        ClientUtils.startTimer(pb,server,mainCtrl);

        Question question = ClientData.getClientQuestion();

        questionTxt.setText(question.getText());

        answer1.setText(question.getFoundActivities().get(0).getTitle());
        answer2.setText(question.getFoundActivities().get(1).getTitle());
        answer3.setText(question.getFoundActivities().get(2).getTitle());
    }

    public void nextQuestion(){
        if(answer1.equals(radioGroup.getSelectedToggle())){
            ClientData.setClientScore(ClientData.getClientScore() + 500);
        }

        ClientUtils.getQuestion(server, mainCtrl);
    }
}
