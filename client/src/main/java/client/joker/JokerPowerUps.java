package client.joker;

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
        System.out.println("Time was halved");
        jokerUtils.setLobbyJoker(JokerType.HALF_TIME_FOR_ALL_LOBBY);
        jokerUtils.sendJoker();
    }
}
