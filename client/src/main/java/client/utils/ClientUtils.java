package client.utils;

import constants.QuestionTypes;
import javafx.scene.control.ProgressBar;

public interface ClientUtils {

    boolean isInLobby();

    void startTimer(ProgressBar pb, Object me, QuestionTypes questionType);

    void halfTime();

    void getQuestion();

    void prepareQuestion();

    Object getCurrentSceneCtrl();

    void setCurrentSceneCtrl(Object currentSceneCtrl);

    double getCoefficient();

    void updateMessages(QuestionTypes q, String str, String lobbyToken);
}
