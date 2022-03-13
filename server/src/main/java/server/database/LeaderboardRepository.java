package server.database;

import commons.LeaderboardEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, Long> {

    @Query("select l from LeaderboardEntry l order by l.score desc")
    List<LeaderboardEntry> findTop10ByOrderByScoreDesc();

}
