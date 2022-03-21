package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import commons.Player;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

@Transactional
public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Modifying
    @Query("update Player u set u.score=?1 where u.id=?2")
    public void updateScore(int score, long id);
}
