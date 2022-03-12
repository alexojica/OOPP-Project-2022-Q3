package client.scenes.questions;

import client.ClientData;
import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Question;
import jakarta.ws.rs.ConstrainedTo;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import static constants.QuestionTypes.ESTIMATION_QUESTION;

public class EstimationQuestionCtrl {

    private final ServerUtils server;

    private final ClientUtils client;

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
    public EstimationQuestionCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.client = client;
    }

    public void nextQuestion(){

        client.getQuestion();
    }

    public void load() {

        scoreTxt.setText("Score:" + ClientData.getClientScore());

        Question question = ClientData.getClientQuestion();

        client.startTimer(pb,this, ESTIMATION_QUESTION);

        questionTxt.setText(question.getText());    
    }

    public void submit() {

    }

    public void leaveGame() {
        client.leaveLobby();
    }
}
