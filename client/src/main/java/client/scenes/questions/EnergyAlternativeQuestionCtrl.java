package client.scenes.questions;

import client.data.ClientData;
import client.utils.ClientUtils;
import commons.Question;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

import javax.inject.Inject;

import static constants.QuestionTypes.ENERGY_ALTERNATIVE_QUESTION;

public class EnergyAlternativeQuestionCtrl {
    private final ClientData clientData;
    private final ClientUtils client;

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

    private int correctAnswer;

    @Inject
    public EnergyAlternativeQuestionCtrl(ClientData clientData, ClientUtils  client) {
        this.clientData = clientData;
        this.client = client;
    }

    public void load() {

        Question question = clientData.getClientQuestion();

        resetUI(question);

    }

    public void leaveGame(){
        client.leaveLobby();
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

        answer1.setToggleGroup(radioGroup);
        answer2.setToggleGroup(radioGroup);
        answer3.setToggleGroup(radioGroup);

        answer1.setStyle(" -fx-background-color: transparent; ");
        answer2.setStyle(" -fx-background-color: transparent; ");
        answer3.setStyle(" -fx-background-color: transparent; ");

        insteadOfText.setText(question.getText() + " " + question.getFoundActivities().get(0).getTitle());

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

    public void randomizeFields(RadioButton a, RadioButton b, RadioButton c, Question question)
    {
        a.setText(question.getFoundActivities().get(1).getTitle());
        b.setText(question.getFoundActivities().get(2).getTitle());
        c.setText(question.getFoundActivities().get(3).getTitle());
    }

    public void nextQuestion(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Platform.runLater(() -> updateCorrectAnswer());
                    //sleep for two seconds to update ui and let the user see the correct answer

                    Thread.sleep(2000);

                    //prepare the question again only if not host
                    if(!clientData.getIsHost()) client.prepareQuestion();

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

        if(clientData.getIsHost())
        {
            //if host prepare next question
            client.prepareQuestion();
        }

        switch (correctAnswer)
        {
            case 0:
                if(answer1.equals(radioGroup.getSelectedToggle())){
                    clientData.setClientScore(clientData.getClientScore() + 500);
                }
                answer1.setStyle(" -fx-background-color: green; ");
                answer2.setStyle(" -fx-background-color: red; ");
                answer3.setStyle(" -fx-background-color: red; ");
                break;
            case 1:
                if(answer2.equals(radioGroup.getSelectedToggle())){
                    clientData.setClientScore(clientData.getClientScore() + 500);
                }
                answer2.setStyle(" -fx-background-color: green; ");
                answer1.setStyle(" -fx-background-color: red; ");
                answer3.setStyle(" -fx-background-color: red; ");
                break;
            case 2:
                if(answer3.equals(radioGroup.getSelectedToggle())){
                    clientData.setClientScore(clientData.getClientScore() + 500);
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
    }
}
