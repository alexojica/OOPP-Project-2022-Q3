package client.utils;

import constants.QuestionTypes;
import javafx.scene.control.ProgressBar;

public interface ClientUtils {

    boolean isInLobby();

    void leaveLobby();

    void startTimer(ProgressBar pb, Object me, QuestionTypes questionType);

    void halfTime();

    void getQuestion();

    void prepareQuestion();

    Object getCurrentSceneCtrl();

    void setCurrentSceneCtrl(Object currentSceneCtrl);

    double getCoefficient();
}
