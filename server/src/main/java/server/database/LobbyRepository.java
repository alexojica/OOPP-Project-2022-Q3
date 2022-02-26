package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import server.entities.Lobby;

import javax.transaction.Transactional;

@Transactional
public interface LobbyRepository extends JpaRepository<Lobby, Integer> {
}
