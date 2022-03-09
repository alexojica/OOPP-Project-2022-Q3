package client.scenes.questions;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class GameMCQCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Text scoreTxt;

    @FXML
    private Text nQuestionsTxt;

    @Inject
    public GameMCQCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void leaveGame(){
        mainCtrl.showHome();
    }
}
