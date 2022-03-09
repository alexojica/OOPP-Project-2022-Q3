package client.scenes;

import client.ClientData;
import client.scenes.menus.MultiplayerMenuCtrl;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class UsernamePopUpCtrl {

    private final MainCtrl mainCtrl;
    private final MultiplayerMenuCtrl multiplayerMenuCtrl;
    private ClientData clientData;

    @FXML
    private TextField name;

    @Inject
    public UsernamePopUpCtrl(MainCtrl mainCtrl, MultiplayerMenuCtrl multiplayerMenuCtrl) {
        this.mainCtrl = mainCtrl;
        this.multiplayerMenuCtrl = multiplayerMenuCtrl;
    }

    public void done(){
        clientData.getClientPlayer().name = name.getText();
        showWaiting();
    }
    public void showWaiting(){
        mainCtrl.closePopUp();
    }
}
