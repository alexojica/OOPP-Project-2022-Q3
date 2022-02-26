package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import server.entities.Player;

import javax.transaction.Transactional;

@Transactional
public interface PlayerRepository extends JpaRepository<Player, Long> {
}
