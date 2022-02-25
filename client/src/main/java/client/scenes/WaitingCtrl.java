package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class WaitingCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TableView tableView;

    @FXML
    private Text tip;

    @FXML
    private Text lobbyCode;

    @Inject
    public WaitingCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Method that sets up how the scene should look like when switched to
     * TODO: Create a class that stores a player (avatar, username...)
     * TODO: Have the 'Tip' text randomised and changed every x seconds
     */
    public void load(){
        tip.setText("Theres only one correct answer per question, get the most right to win.");
        lobbyCode.setText(lobbyCode.getText() + 59864);
    }

    public void leaveLobby(){

    }

    public void startGame(){
        mainCtrl.showGameMCQ();
    }
}
