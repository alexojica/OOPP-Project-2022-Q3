package client.scenes.questions;

import client.data.ClientData;
import client.joker.JokerPowerUps;
import client.joker.JokerUtils;
import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Player;
import commons.Question;
import commons.WebsocketMessage;
import constants.ResponseCodes;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.util.Random;

import static constants.QuestionTypes.MULTIPLE_CHOICE_QUESTION;

public class GameMCQCtrl extends JokerPowerUps {

    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;
    private final ClientData clientData;

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

    private int correctAnswer;

    @Inject
    public GameMCQCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl, ClientData clientData,
                       JokerUtils jokerUtils) {
        super(jokerUtils);
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.client = client;
        this.clientData = clientData;
    }

    public void leaveGame(){
        client.leaveLobby();
    }


    public void load() {

        Question question = clientData.getClientQuestion();

        resetUI(question);

    }

    public void resetUI(Question question)
    {
        scoreTxt.setText("Score:" + clientData.getClientScore());
        nQuestionsTxt.setText(clientData.getQuestionCounter() + "/20");

        answer1.setToggleGroup(radioGroup);
        answer2.setToggleGroup(radioGroup);
        answer3.setToggleGroup(radioGroup);

        answer1.setStyle(" -fx-background-color: transparent; ");
        answer2.setStyle(" -fx-background-color: transparent; ");
        answer3.setStyle(" -fx-background-color: transparent; ");


        if(answer1.isSelected()) answer1.setSelected(false);
        if(answer2.isSelected()) answer2.setSelected(false);
        if(answer3.isSelected()) answer3.setSelected(false);

        client.startTimer(pb,this, MULTIPLE_CHOICE_QUESTION);

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
    }

    public void randomizeFields(RadioButton a, RadioButton b, RadioButton c, Question question)
    {
        a.setText(question.getFoundActivities().get(0).getTitle());
        b.setText(question.getFoundActivities().get(1).getTitle());
        c.setText(question.getFoundActivities().get(2).getTitle());
    }

    public void nextQuestion(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Platform.runLater(() -> updateCorrectAnswer());
                    //sleep for two seconds to update ui and let the user see the correct answer

                    Thread.sleep(2000);



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
                    clientData.getClientPlayer().score = clientData.getClientScore();

                    server.send("/app/updateScore", new WebsocketMessage(ResponseCodes.SCORE_UPDATED,
                            clientData.getClientLobby().getToken(), clientData.getClientPlayer()));
                }
                answer1.setStyle(" -fx-background-color: green; ");
                answer2.setStyle(" -fx-background-color: red; ");
                answer3.setStyle(" -fx-background-color: red; ");
                break;
            case 1:
                if(answer2.equals(radioGroup.getSelectedToggle())){
                    clientData.setClientScore(clientData.getClientScore() +
                            (int) (pointsToAdd* client.getCoefficient()));
                    clientData.getClientPlayer().score = clientData.getClientScore();

                    server.send("/app/updateScore", new WebsocketMessage(ResponseCodes.SCORE_UPDATED,
                            clientData.getClientLobby().getToken(), clientData.getClientPlayer()));
                }
                answer2.setStyle(" -fx-background-color: green; ");
                answer1.setStyle(" -fx-background-color: red; ");
                answer3.setStyle(" -fx-background-color: red; ");
                break;
            case 2:
                if(answer3.equals(radioGroup.getSelectedToggle())){
                    clientData.setClientScore(clientData.getClientScore() +
                            (int) (pointsToAdd* client.getCoefficient()));
                    clientData.getClientPlayer().score = clientData.getClientScore();

                    server.send("/app/updateScore", new WebsocketMessage(ResponseCodes.SCORE_UPDATED,
                            clientData.getClientLobby().getToken(), clientData.getClientPlayer()));
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

        Player temp = clientData.getClientPlayer();
        temp.setScore(Math.toIntExact(clientData.getClientScore()));
        server.updateScore(temp);
    }

    /**
     * Get a random answer and if:
     * a) the answer is wrong, mark it as disabled
     * b) if the answer is the correct one, disable the answer after that (which will b a wrong one)
     */
    public void eliminateRandomWrongAnswer(){
        int indexToRemove = new Random().nextInt(3);
        if(indexToRemove == correctAnswer){
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

    public RadioButton getAnswer1() {
        return answer1;
    }


    public RadioButton getAnswer2() {
        return answer2;
    }


    public RadioButton getAnswer3() {
        return answer3;
    }

}
