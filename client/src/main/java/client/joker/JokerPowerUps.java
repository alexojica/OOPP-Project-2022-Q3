package client.joker;

import client.utils.ClientUtils;
import constants.JokerType;

import javax.inject.Inject;

public abstract class JokerPowerUps {
    protected boolean doublePoints = false;
    private JokerUtils jokerUtils;

    @Inject
    public JokerPowerUps(JokerUtils jokerUtils) {
        this.jokerUtils = jokerUtils;
    }

    public void doublePoints(){
        doublePoints = true;
    }

    public void halfTimeForOthers(){
        jokerUtils.setLobbyJoker(JokerType.HALF_TIME_FOR_ALL_LOBBY);
        jokerUtils.sendJoker();
    }
}
