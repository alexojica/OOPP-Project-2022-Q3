package server.database;

import commons.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;


@Transactional
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("select u from Question u where u.id = ?1")
    public Optional<Question> findByPointer(Long pointer);

    public long count();
}
