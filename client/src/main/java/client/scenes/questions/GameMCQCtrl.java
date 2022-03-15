package client.scenes.questions;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class GameMCQCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Text scoreTxt;

    @FXML
    private Text nQuestionsTxt;

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
        mainCtrl.showGameModeSelection();
    }

    public void load() {
        answer1.setText(server.getRandomActivity().getTitle());
        answer2.setText(server.getRandomActivity().getTitle());
        answer3.setText(server.getRandomActivity().getTitle());
    }
}
