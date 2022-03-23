package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import commons.Player;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

@Transactional
public interface PlayerRepository extends JpaRepository<Player, Long> {

    /**
     * Given the id and the new score of the Player
     * edit the score of the player to the new one if the player was found
     * @param score
     * @param id
     */
    @Modifying
    @Query("update Player u set u.score=?1 where u.id=?2")
    public void updateScore(int score, long id);
}
