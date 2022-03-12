package client.utils;

import constants.QuestionTypes;
import javafx.scene.control.ProgressBar;

public interface ClientUtils {

    boolean isInLobby();

    void leaveLobby();

    void startTimer(ProgressBar pb, Object me, QuestionTypes questionType);

    void getQuestion();
}
