package client.scenes.questions;

import client.data.ClientData;
import client.game.Game;
import client.joker.JokerPowerUps;
import client.joker.JokerUtils;
import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import commons.Question;
import commons.WebsocketMessage;
import constants.ResponseCodes;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import static constants.QuestionTypes.ESTIMATION_QUESTION;

public class EstimationQuestionCtrl extends JokerPowerUps{

    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;
    private final ClientData clientData;
    private final Game game;

    private Double progress;

    @FXML
    private ProgressBar pb;

    @FXML
    private Text scoreTxt;

    @FXML
    private Text nQuestionsTxt;

    @FXML
    private Text questionTxt;

    @FXML
    private Text activityText;

    @FXML
    private TextField answer;

    @FXML
    private Text answerPopUp;

    private Long submittedAnswer;
    private Long correctAnswer;

    @FXML
    private Label messageTxt1;
    @FXML
    private Label messageTxt2;
    @FXML
    private Label messageTxt3;

    @Inject
    public EstimationQuestionCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl, ClientData clientData,
                                  JokerUtils jokerUtils, Game game) {
        super(jokerUtils);
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.client = client;
        this.clientData = clientData;
        this.game = game;
    }

    public void load() {

        Question question = clientData.getClientQuestion();

        resetUI(question);
    }

    public void resetUI(Question question)
    {
        scoreTxt.setText("Score:" + clientData.getClientScore());
        nQuestionsTxt.setText(clientData.getQuestionCounter() + "/20");

        Activity polledActivity = server.getActivityByID(question.getFoundActivities().get(0)).get();
        correctAnswer = polledActivity.getEnergyConsumption();

        answerPopUp.setStyle(" -fx-background-color: transparent; ");
        submittedAnswer = null;

        answer.setText("");
        answer.setStyle(" -fx-background-color: white; ");

        client.startTimer(pb,this, ESTIMATION_QUESTION);

        questionTxt.setText(question.getText());
        activityText.setText(polledActivity.getTitle());
    }

    public void nextQuestion(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Platform.runLater(() -> updateCorrectAnswer());
                    //sleep for two seconds to update ui and let the user see the correct answer

                    Thread.sleep(2000);

                    if(clientData.getQuestionCounter() == 3){
                        Platform.runLater(() -> mainCtrl.showTempLeaderboard());
                        Thread.sleep(5000);
                    }

                    //execute next question immediatly after sleep on current thread finishes execution
                    Platform.runLater(() -> client.getQuestion());
                    //client.getQuestion();
                }catch (InterruptedException e)
                {
                    e.printStackTrace();
                    System.out.println("Something went wrong when showing correct answer!");
                }
            }
        });
        thread.start();
    }

    private void updateCorrectAnswer() {

        if(clientData.getIsHost()){
            //send a new question request to server so it has time to generate it
            server.send("/app/nextQuestion",
                    new WebsocketMessage(ResponseCodes.NEXT_QUESTION,
                            clientData.getClientLobby().getToken(), clientData.getClientPointer()));
        }

        if(submittedAnswer == null) {
            showStatus("No answer submitted!","red");
            //also check for inactivity later on
        }
        else
        {
            showStatus("Correct answer is " + correctAnswer.toString(),"green");
            addPoints();
        }
    }

    public void submit() {
        try {
            submittedAnswer = Long.parseLong(answer.getText());
            progress = pb.getProgress();
            answer.setStyle(" -fx-background-color: yellow; ");
        }catch (NumberFormatException e){
            System.out.println("Number not formatted correctly");
            showStatus("Number not formatted correctly","red");
            answer.setText("");
        }
    }

    //TODO: Right now the points are calculated using simple if -
    // statements, but we should probably do this with a math formula
    public void addPoints()
    {
        Long pointsToAdd = 0L;
        if(correctAnswer * 70 / 100L <= submittedAnswer && submittedAnswer <= correctAnswer * 130/100L)
        {
            //30% off -> get full points
            pointsToAdd = doublePoints ? 1000L : 500L;
        }
        else
        if(correctAnswer * 50 / 100L <= submittedAnswer && submittedAnswer <= correctAnswer * 150/100L)
        {
            //50% off -> get 350 points
            pointsToAdd = doublePoints ? 700L : 350L;
        }
        else
        if(correctAnswer * 30 / 100L <= submittedAnswer && submittedAnswer <= correctAnswer * 170/100L)
        {
            //70% off -> get 250 points
            pointsToAdd = doublePoints ? 500L : 250L;
        }
        else
        if(correctAnswer * 1 / 2L <= submittedAnswer && submittedAnswer <= correctAnswer * 200/100L)
        {
            //100% off -> get 150 points
            pointsToAdd = doublePoints ? 300L : 150L;
        }
        doublePoints = false;
        clientData.setClientScore((int) (clientData.getClientScore() + pointsToAdd * progress));
        scoreTxt.setText("Score:" + clientData.getClientScore());

        clientData.getClientPlayer().score = clientData.getClientScore();
        server.send("/app/updateScore", new WebsocketMessage(ResponseCodes.SCORE_UPDATED,
                clientData.getClientLobby().getToken(), clientData.getClientPlayer()));
    }

    public void showStatus(String text,String color)
    {
        answerPopUp.setText(text);
        answerPopUp.setStyle(" -fx-background-color: " + color + "; ");
    }

    public void leaveGame() {
        game.leaveLobby();
    }

    /**
     * Returns the label corresponding to the position in the method name.
     * @return label corresponding to the position
     */
    public Label getMessageTxt1() {
        return messageTxt1;
    }

    public Label getMessageTxt2() {
        return messageTxt2;
    }

    public Label getMessageTxt3() {
        return messageTxt3;
    }

    /**
     * Sets the label text to the given string and when said string is not empty,
     * a background colour is also added to make the message stand out more.
     * @param message message to be displayed in the label corresponding to the method name
     */
    //empty string check might be used later in order to make messages disappear after X time
    public void setMessageTxt1(String message) {
        messageTxt1.setText(message);
        if(!(message.equals(""))){
            messageTxt1.setStyle("-fx-background-color: darkgray; -fx-padding: 10px");
        }
    }

    public void setMessageTxt2(String message) {
        messageTxt2.setText(message);
        if(!(message.equals(""))){
            messageTxt2.setStyle("-fx-background-color: darkgray; -fx-padding: 10px");
        }
    }

    public void setMessageTxt3(String message) {
        messageTxt3.setText(message);
        if(!(message.equals(""))){
            messageTxt3.setStyle("-fx-background-color: darkgray; -fx-padding: 10px");
        }
    }

    /**
     * Button that sends a websocketmessage containing a questiontype corresponding to the current question,
     * a string containing the playername and an emote and another string containing the player's lobbyToken.
     * This button is a test to see whether the labels are changed properly. It will be removed when
     * branch 75 containing the actual emotes is merged.
     */
    public void testSend() {
        server.send("/app/updateMessages",
                new WebsocketMessage(ESTIMATION_QUESTION, clientData.getClientPlayer().getName()
                        + ": " + new String(Character.toChars(0x1F35D)),
                        clientData.getClientLobby().getToken()));
    }

}
