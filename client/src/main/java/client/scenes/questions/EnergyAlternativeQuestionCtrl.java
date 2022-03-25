package client.scenes.questions;

import client.data.ClientData;
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
import emotes.Emotes;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import javax.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static constants.QuestionTypes.ENERGY_ALTERNATIVE_QUESTION;
import static javafx.scene.paint.Color.rgb;

public class EnergyAlternativeQuestionCtrl implements JokerPowerUps {
    private final ClientData clientData;
    private final ClientUtils client;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final Emotes emotes;
    private final Game game;

    @FXML
    private Text scoreTxt;

    @FXML
    private Text nQuestionsTxt;

    @FXML
    private ProgressBar pb;

    @FXML
    private Text insteadOfText;

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
    protected boolean doublePoints = false;
    private JokerUtils jokerUtils;

    @FXML
    private MenuButton emotesMenu;

    @FXML
    private Label messageTxt1;
    @FXML
    private Label messageTxt2;
    @FXML
    private Label messageTxt3;

    @Inject
    public EnergyAlternativeQuestionCtrl(ClientData clientData, ClientUtils  client, ServerUtils server,
                                         JokerUtils jokerUtils, Emotes emotes, Game game, MainCtrl mainCtrl) {
        this.jokerUtils = jokerUtils;
        this.clientData = clientData;
        this.client = client;
        this.server = server;
        this.game = game;
        this.mainCtrl = mainCtrl;
        this.emotes = emotes;
    }

    public void load() {
        if(client.isInLobby()) {
            setUpEmoteMenu();
            Question question = clientData.getClientQuestion();
            resetUI(question);
        }
    }

    public void leaveGame(){
        game.leaveLobby();
    }

    /**
     * Setting up the UI for this scene. Note that:
     * the first activity returned from the server is the 'instead of' activity
     * the second is the correct alternative
     * the third and fourth are the wrong alternatives
     * @param question
     */
    private void resetUI(Question question) {
        scoreTxt.setText("Score:" + clientData.getClientScore());
        nQuestionsTxt.setText(clientData.getQuestionCounter() + "/20");
        doublePoints = false;
        joker3.setDisable(clientData.getUsedJokers().contains(JokerType.HALF_TIME_FOR_ALL_LOBBY));
        joker1.setDisable(clientData.getUsedJokers().contains(JokerType.DOUBLE_POINTS));
        joker2.setDisable(clientData.getUsedJokers().contains(JokerType.ELIMINATE_ANSWERS));

        answer1.setToggleGroup(radioGroup);
        answer2.setToggleGroup(radioGroup);
        answer3.setToggleGroup(radioGroup);

        answer1.setDisable(false);
        answer2.setDisable(false);
        answer3.setDisable(false);
        if(!clientData.getUsedJokers().contains(JokerType.DOUBLE_POINTS))
            joker1.setFill(rgb(30,144,255));
        else
            joker1.setFill(rgb(235,235,228));

        if(!clientData.getUsedJokers().contains(JokerType.ELIMINATE_ANSWERS))
            joker2.setFill(rgb(30,144,255));
        else
            joker2.setFill(rgb(235,235,228));

        if(!clientData.getUsedJokers().contains(JokerType.HALF_TIME_FOR_ALL_LOBBY))
            joker3.setFill(rgb(30,144,255));
        else
            joker3.setFill(rgb(235,235,228));


        answer1.setStyle(" -fx-background-color: transparent; ");
        answer2.setStyle(" -fx-background-color: transparent; ");
        answer3.setStyle(" -fx-background-color: transparent; ");

        Optional<Activity> act = server.getActivityByID(question.getFoundActivities().get(0));
        String textMethod = question.getText();
        if(act.isPresent()) {
            textMethod += " " + act.get().getTitle();
        }
        insteadOfText.setText(textMethod);

        if(answer1.isSelected()) answer1.setSelected(false);
        if(answer2.isSelected()) answer2.setSelected(false);
        if(answer3.isSelected()) answer3.setSelected(false);

        client.startTimer(pb,this, ENERGY_ALTERNATIVE_QUESTION);

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
    }

    public void disableAnswers(){
        answer1.setDisable(true);
        answer2.setDisable(true);
        answer3.setDisable(true);
    }

    public void randomizeFields(RadioButton a, RadioButton b, RadioButton c, Question question)
    {
        List<Activity> list = server.getActivitiesFromIDs(question.getFoundActivities());
        a.setText(list.get(1).getTitle());
        b.setText(list.get(2).getTitle());
        c.setText(list.get(3).getTitle());
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
            server.send("/app/nextQuestion",
                    new WebsocketMessage(ResponseCodes.NEXT_QUESTION,
                            clientData.getClientLobby().getToken(), clientData.getClientPointer()));
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
                //no answer was selected do nothing
                //maybe poll later for inactivity
                break;
        }
        scoreTxt.setText("Score:" + clientData.getClientScore());

        clientData.getClientPlayer().score = clientData.getClientScore();
        server.send("/app/updateScore", new WebsocketMessage(ResponseCodes.SCORE_UPDATED,
                clientData.getClientLobby().getToken(), clientData.getClientPlayer()));
    }

    public void eliminateRandomWrongAnswer() {
        if(!clientData.getUsedJokers().contains(JokerType.ELIMINATE_ANSWERS)) {
            joker2.setDisable(true);
            joker2.setFill(rgb(235,235,228));
            int indexToRemove = new Random().nextInt(3);
            if (indexToRemove == correctAnswer) {
                indexToRemove++;
            }
            switch (indexToRemove) {
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
