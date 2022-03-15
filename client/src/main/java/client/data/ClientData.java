package client.data;

import commons.Lobby;
import commons.Player;
import commons.Question;

public interface ClientData {

    Boolean getIsHost();

    void setAsHost(Boolean value);

    Integer getQuestionCounter();

    void incrementQuestionCounter();

    void setQuestionCounter(Integer counter);

    Long getClientScore();

    void setClientScore(Long score);

    void setPlayer(Player player);

    Player getClientPlayer();

    void setPointer(Long pointer);

    Long getClientPointer();

    void setQuestion(Question question);

    Question getClientQuestion();

    void setLobby(Lobby lobby);

    Lobby getClientLobby();
}
