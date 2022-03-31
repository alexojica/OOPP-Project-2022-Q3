package client.scenes.questions;

import client.data.ClientData;
import client.emotes.Emotes;
import client.game.Game;
import client.joker.JokerPowerUps;
import client.joker.JokerUtils;
import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Activity;
import commons.Question;
import commons.WebsocketMessage;
import constants.JokerType;
import constants.ResponseCodes;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.util.List;
import java.util.Random;

import static constants.QuestionTypes.GUESS_X;
import static javafx.scene.paint.Color.rgb;

public class GuessConsumptionCtrl implements JokerPowerUps {

    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;
    private final ClientData clientData;
    private final Emotes emotes;
    private final Game game;
    protected boolean doublePoints = false;
    private JokerUtils jokerUtils;

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
    @FXML
    private Circle joker1;
    @FXML
    private Circle joker2;
    @FXML
    private Circle joker3;

    private int correctAnswer;

    @FXML
    private MenuButton emotesMenu;

    @FXML
    private Label messageTxt1;
    @FXML
    private Label messageTxt2;
    @FXML
    private Label messageTxt3;

    @FXML
    private ImageView imageView;

    @FXML
    private Text activityText;

    @Inject
    public GuessConsumptionCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl, ClientData clientData,
                       JokerUtils jokerUtils, Emotes emotes, Game game) {
        this.jokerUtils = jokerUtils;
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.client = client;
        this.clientData = clientData;
        this.emotes = emotes;
        this.game = game;
    }

    public void leaveGame(){
        game.leaveLobby();
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
        joker2.setDisable(clientData.getUsedJokers().contains(JokerType.ELIMINATE_ANSWERS));

        if(!clientData.getUsedJokers().contains(JokerType.DOUBLE_POINTS))
            joker1.setFill(rgb(30,144,255));
        if(!clientData.getUsedJokers().contains(JokerType.ELIMINATE_ANSWERS))
            joker2.setFill(rgb(30,144,255));
        if(!clientData.getUsedJokers().contains(JokerType.HALF_TIME_FOR_ALL_LOBBY))
            joker3.setFill(rgb(30,144,255));

        answer1.setToggleGroup(radioGroup);
        answer2.setToggleGroup(radioGroup);
        answer3.setToggleGroup(radioGroup);

        answer1.setDisable(false);
        answer2.setDisable(false);
        answer3.setDisable(false);

        answer1.setStyle(" -fx-background-color: transparent; ");
        answer2.setStyle(" -fx-background-color: transparent; ");
        answer3.setStyle(" -fx-background-color: transparent; ");


        if(answer1.isSelected()) answer1.setSelected(false);
        if(answer2.isSelected()) answer2.setSelected(false);
        if(answer3.isSelected()) answer3.setSelected(false);

        client.startTimer(pb,this, GUESS_X);

        questionTxt.setText(question.getText());

        Random random = new Random();
        correctAnswer = random.nextInt(3);

        switch (correctAnswer)
        {
            case 0:
                //correct answer is first one
                randomizeFields(answer1,answer2,answer3,question);
                break;
            case 1:
                //correct answer is second one
                randomizeFields(answer2,answer1,answer3,question);
                break;
            case 2:
                //correct answer is third one
                randomizeFields(answer3,answer1,answer2,question);
                break;
            default:
                break;
        }
        Activity polledActivity = server.getActivityByID(question.getFoundActivities().get(0)).get();
        questionTxt.setText(question.getText());
        activityText.setText(polledActivity.getTitle());
        Image image = server.getImageFromActivity(polledActivity);
        imageView.setImage(image);
    }

    public void randomizeFields(RadioButton a, RadioButton b, RadioButton c, Question question)
    {
        List<Activity> list = server.getActivitiesFromIDs(question.getFoundActivities());
        a.setText(String.valueOf(list.get(0).getEnergyConsumption()));
        b.setText(String.valueOf(list.get(1).getEnergyConsumption()));
        c.setText(String.valueOf(list.get(2).getEnergyConsumption()));
    }

    public void disableAnswers(){
        answer1.setDisable(true);
        answer2.setDisable(true);
        answer3.setDisable(true);
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

    public void updateCorrectAnswer()
    {

        int pointsToAdd = doublePoints ? 1000 : 500;
        doublePoints = false;

        if(clientData.getIsHost())
        {
            //if host prepare next question
            //client.prepareQuestion();

            server.send("/app/nextQuestion",
                    new WebsocketMessage(ResponseCodes.NEXT_QUESTION,
                            clientData.getClientLobby().token, clientData.getClientPointer()));
        }

        switch (correctAnswer)
        {
            case 0:
                if(answer1.equals(radioGroup.getSelectedToggle())){
                    clientData.setClientScore(clientData.getClientScore() +
                            (int) (pointsToAdd* client.getCoefficient()));
                }
                answer1.setStyle(" -fx-background-color: green; ");
                answer2.setStyle(" -fx-background-color: red; ");
                answer3.setStyle(" -fx-background-color: red; ");
                break;
            case 1:
                if(answer2.equals(radioGroup.getSelectedToggle())){
                    clientData.setClientScore(clientData.getClientScore() +
                            (int) (pointsToAdd* client.getCoefficient()));
                }
                answer2.setStyle(" -fx-background-color: green; ");
                answer1.setStyle(" -fx-background-color: red; ");
                answer3.setStyle(" -fx-background-color: red; ");
                break;
            case 2:
                if(answer3.equals(radioGroup.getSelectedToggle())){
                    clientData.setClientScore(clientData.getClientScore() +
                            (int) (pointsToAdd* client.getCoefficient()));
                }
                answer3.setStyle(" -fx-background-color: green; ");
                answer1.setStyle(" -fx-background-color: red; ");
                answer2.setStyle(" -fx-background-color: red; ");
                break;
            default:
                break;
        }
        if(answer3.isSelected() == false && answer2.isSelected() == false && answer1.isSelected() == false){
            clientData.incrementUnansweredQuestionCounter();
            if(clientData.getUnansweredQuestionCounter() >= 5){
                leaveGame();
            }
        }
        scoreTxt.setText("Score:" + clientData.getClientScore());

        clientData.getClientPlayer().score = clientData.getClientScore();
        server.send("/app/updateScore", new WebsocketMessage(ResponseCodes.SCORE_UPDATED,
                clientData.getClientLobby().getToken(), clientData.getClientPlayer()));
    }

    /**
     * Get a random answer and if:
     * a) the answer is wrong, mark it as disabled
     * b) if the answer is the correct one, disable the answer after that (which will b a wrong one)
     */
    public void eliminateRandomWrongAnswer(){
        if(!clientData.getUsedJokers().contains(JokerType.ELIMINATE_ANSWERS)) {
            clientData.addJoker(JokerType.ELIMINATE_ANSWERS);
            joker2.setFill(rgb(235,235,228));
            joker2.setDisable(true);
            int indexToRemove = new Random().nextInt(3);
            if (indexToRemove == correctAnswer) {
                indexToRemove++;
            }
            switch (indexToRemove % 3) {
                case 0:
                    answer1.setStyle(" -fx-background-color: red; ");
                    System.out.println("Disabled first answer");
                    break;
                case 1:
                    answer2.setStyle(" -fx-background-color: red; ");
                    System.out.println("Disabled second answer");
                    break;
                case 2:
                    answer3.setStyle(" -fx-background-color: red; ");
                    System.out.println("Disabled third answer");
                    break;
            }
        }
    }

    public RadioButton getAnswer1() {
        return answer1;
    }


    public RadioButton getAnswer2() {
        return answer2;
    }


    public RadioButton getAnswer3() {
        return answer3;
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
}
