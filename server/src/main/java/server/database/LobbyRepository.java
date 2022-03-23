package server.database;

import org.springframework.data.jpa.repository.JpaRepository;
import commons.Lobby;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface LobbyRepository extends JpaRepository<Lobby, Long> {

    /**
     * Given the token of the lobby object, find that lobby
     * @param token
     * @return the lobby object if it was found
     */
    @Query("select u from Lobby u where u.token = ?1")
    public Optional<Lobby> findByToken(String token);
}
