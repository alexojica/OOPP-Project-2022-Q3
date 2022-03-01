package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import commons.Lobby;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface LobbyRepository extends JpaRepository<Lobby, Long> {

    @Query("select u from Lobby u where u.token = ?1")
    public Optional<Lobby> findByToken(String token);
}
