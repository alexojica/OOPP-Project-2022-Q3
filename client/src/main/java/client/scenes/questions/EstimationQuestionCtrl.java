package client.scenes.questions;

import client.ClientData;
import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Question;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

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
