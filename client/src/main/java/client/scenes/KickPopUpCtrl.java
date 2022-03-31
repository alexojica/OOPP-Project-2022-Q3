package client.scenes;

import javax.inject.Inject;

public class KickPopUpCtrl {

    private final MainCtrl mainCtrl;

    @Inject
    public KickPopUpCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void ok(){
        showGameModeSelection();
    }

    public void showGameModeSelection(){
        mainCtrl.closeKickPopUp();
    }
}
