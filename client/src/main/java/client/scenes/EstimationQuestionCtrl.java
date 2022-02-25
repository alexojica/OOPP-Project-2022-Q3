package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class EstimationQuestionCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField answer;

    @Inject
    public EstimationQuestionCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void submit() {
        //should send answer from answer field to somewhere (and then disable)
    }

    public void leaveGame() {
        mainCtrl.showOverview(); //should go to splash screen
    }
}
