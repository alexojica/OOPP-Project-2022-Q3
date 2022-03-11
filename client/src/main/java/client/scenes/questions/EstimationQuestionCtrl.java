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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;

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

    private void nextQuestion(){

        ClientUtils.getQuestion(server, mainCtrl);

        return;
    }

    public void load() {

        scoreTxt.setText("Score:" + ClientData.getClientScore());

        Question question = ClientData.getClientQuestion();

        IntegerProperty seconds = new SimpleIntegerProperty();
        pb.progressProperty().bind(seconds.divide(20.0));
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(seconds, 0)),
            new KeyFrame(Duration.minutes((double) 1.0 * 1/3), e-> {
                nextQuestion();
            }, new KeyValue(seconds, 20))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        questionTxt.setText(question.getText());    
    }

    public void submit() {

    }

    public void leaveGame() {
        ClientUtils.leaveLobby(server, mainCtrl);
    }
}
