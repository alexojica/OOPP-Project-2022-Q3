package server.database;

import commons.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;


@Transactional
public interface QuestionRepository extends JpaRepository<Question, Long> {

    /**
     * In order to find the next question that will be fetched
     * The pointer from the old question is taken as a parameter
     * which gives the id of the next question
     * @param pointer
     * @param lastLobby
     * @return the new question with the id equal to the old question's pointer
     */
    Optional<Question> findByIdAndLastLobbyToken(Long pointer, String lastLobby);
    long count();
}
