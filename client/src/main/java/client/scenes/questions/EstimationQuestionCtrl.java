package client.scenes.questions;

import client.data.ClientData;
import client.emotes.Emotes;
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
import constants.JokerType;
import constants.ResponseCodes;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import static constants.QuestionTypes.ESTIMATION_QUESTION;
import static javafx.scene.paint.Color.rgb;

public class EstimationQuestionCtrl implements JokerPowerUps{

    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;
    private final ClientData clientData;
    private final Emotes emotes;
    private final Game game;
    protected boolean doublePoints = false;
    private JokerUtils jokerUtils;

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

    @FXML
    private Button submit;

    @FXML
    private Circle joker1;

    @FXML
    private Circle joker3;

    private Long submittedAnswer;
    private Long correctAnswer;

    @FXML
    private MenuButton emotesMenu;
    
    @FXML
    private Label messageTxt1;
    @FXML
    private Label messageTxt2;
    @FXML
    private Label messageTxt3;

    @Inject
    public EstimationQuestionCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl, ClientData clientData,
                                  JokerUtils jokerUtils, Emotes emotes, Game game) {
        this.jokerUtils = jokerUtils;
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.client = client;
        this.clientData = clientData;
        this.game = game;
        this.emotes = emotes;
    }

    public void load() {
        if(client.isInLobby()) {
            setUpEmoteMenu();
            Question question = clientData.getClientQuestion();
            resetUI(question);
        }
    }

    public void resetUI(Question question)
    {
        scoreTxt.setText("Score:" + clientData.getClientScore());
        nQuestionsTxt.setText(clientData.getQuestionCounter() + "/20");

        doublePoints = false;
        joker3.setDisable(clientData.getUsedJokers().contains(JokerType.HALF_TIME_FOR_ALL_LOBBY));
        joker1.setDisable(clientData.getUsedJokers().contains(JokerType.DOUBLE_POINTS));

        submit.setDisable(false);

        if(!clientData.getUsedJokers().contains(JokerType.DOUBLE_POINTS))
            joker1.setFill(rgb(30,144,255));
        else
            joker1.setFill(rgb(235,235,228));

        if(!clientData.getUsedJokers().contains(JokerType.HALF_TIME_FOR_ALL_LOBBY))
            joker3.setFill(rgb(30,144,255));
        else
            joker3.setFill(rgb(235,235,228));


        Activity polledActivity = server.getActivityByID(question.getFoundActivities().get(0)).get();
        correctAnswer = polledActivity.getEnergyConsumption();

        answerPopUp.setText("");
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

                    if(clientData.getQuestionCounter() == game.getQuestionsToDisplayLeaderboard()){
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

    public void disableSubmitButton(){
        submit.setDisable(true);
    }

    @Override
    public void doublePoints() {
        if(!clientData.getUsedJokers().contains(JokerType.DOUBLE_POINTS)) {
            doublePoints = true;
            joker1.setDisable(true);
            joker1.setFill(rgb(235,235,228));
            clientData.addJoker(JokerType.DOUBLE_POINTS);
        }
    }

    @Override
    public void halfTimeForOthers() {
        if(!clientData.getUsedJokers().contains(JokerType.HALF_TIME_FOR_ALL_LOBBY)) {
            joker3.setDisable(true);
            joker3.setFill(rgb(235,235,228));
            clientData.addJoker(JokerType.HALF_TIME_FOR_ALL_LOBBY);
            System.out.println("Time was halved");
            jokerUtils.setLobbyJoker(JokerType.HALF_TIME_FOR_ALL_LOBBY);
            jokerUtils.sendJoker();
        }
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
     * This background colour is removed however when the string is empty in order to reset.
     * @param message message to be displayed in the label corresponding to the method name
     */
    //empty string check might be used later in order to make messages disappear after X time
    public void setMessageTxt1(String message) {
        messageTxt1.setText(message);
        if(!(message.equals(""))){
            messageTxt1.setStyle("-fx-background-color: darkgray; -fx-padding: 10px");
        }
        else{
            messageTxt1.setStyle("-fx-background-color: none; -fx-padding: none");
        }
    }

    public void setMessageTxt2(String message) {
        messageTxt2.setText(message);
        if(!(message.equals(""))){
            messageTxt2.setStyle("-fx-background-color: darkgray; -fx-padding: 10px");
        }
        else{
            messageTxt2.setStyle("-fx-background-color: none; -fx-padding: none");
        }
    }

    public void setMessageTxt3(String message) {
        messageTxt3.setText(message);
        if(!(message.equals(""))){
            messageTxt3.setStyle("-fx-background-color: darkgray; -fx-padding: 10px");
        }
        else{
            messageTxt3.setStyle("-fx-background-color: none; -fx-padding: none");
        }
    }

    /**
     * Sets up the emoteMenu menubutton by first clearing anything that might be left in it from previous calls.
     * This is done to prevent errors from occurring. Then all the emotes from the list in the EmotesImpl class are
     * added and set such they trigger the sendEmote method when clicked. There's also some padding added to make it
     * easier to click the buttons.
     */
    public void setUpEmoteMenu(){
        emotesMenu.getItems().clear();
        emotesMenu.getItems().addAll(emotes.getEmotesList());
        for(MenuItem m : emotesMenu.getItems()){
            m.setStyle("-fx-padding: 0 25 0 25");
            m.setOnAction(a -> {
                emotes.sendEmote(m.getText());
            });
        }
    }
}