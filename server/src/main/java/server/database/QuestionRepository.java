package server.database;

import commons.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;


@Transactional
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findById(Long pointer);

    long count();
}
