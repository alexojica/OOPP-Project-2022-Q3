package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import server.entities.LeaderboardEntry;

import javax.transaction.Transactional;

@Transactional
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntry, Long> {
}
