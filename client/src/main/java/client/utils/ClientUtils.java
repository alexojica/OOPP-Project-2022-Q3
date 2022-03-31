package client.utils;

import constants.QuestionTypes;
import javafx.scene.control.ProgressBar;

public interface ClientUtils {

    boolean isInLobby();

    void startTimer(ProgressBar pb, Object me, QuestionTypes questionType);

    void halfTime();

    void getQuestion();

    void prepareQuestion();

    void killTimer();

    void unsubscribeFromMessages();

    void registerLobbyCommunication();

    void registerQuestionCommunication();

    void registerMessageCommunication();

    Object getCurrentSceneCtrl();

    void setCurrentSceneCtrl(Object currentSceneCtrl);

    double getCoefficient();

    void updateMessages(String str, String lobbyToken);

    void resetMessages();

    void swapEmoteJokerUsability(boolean bool);
}
