package server.database;

import commons.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class PlayerRepositoryTest {
    @Autowired
    private PlayerRepository playerRepository;

    @BeforeEach
    void initUseCase() {
        List<Player> activities = Arrays.asList(
                new Player("andi"), new Player("johnny", "somepath")
        );
        playerRepository.saveAll(activities);
    }

    @AfterEach
    public void destroyAll(){
        playerRepository.deleteAll();
    }

    @Test
    void saveAll_success() {
        List<Player> players = Arrays.asList(
                new Player("testPlayer", "someotherpath"),
                new Player("testPlayer2"),
                new Player("testPlayer3", "somepath")
        );
        Iterable<Player> allCustomer = playerRepository.saveAll(players);

        AtomicInteger validIdFound = new AtomicInteger();
        allCustomer.forEach(player -> {
            if(player.id > 1){
                validIdFound.getAndIncrement();
            }
        });

        assertThat(validIdFound.intValue()).isEqualTo(3);
    }

    @Test
    void findAll_success() {
        List<Player> allPlayer = playerRepository.findAll();
        assertThat(allPlayer.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void findNoting(){
        playerRepository.deleteAll();
        assertThat(playerRepository.findAll().size() == 0);
    }

    @Test
    void findById(){
        long id = playerRepository.findAll().get(0).id;
        Player player = playerRepository.findById(id).get();
        assertTrue(player != null);
        assertTrue(player.getName().equals("andi"));
        id = playerRepository.findAll().get(1).id;
        player = playerRepository.findById(id).get();
        assertTrue(player != null);
        assertTrue(player.getName().equals("johnny"));
        assertTrue(player.getAvatar().equals("somepath"));
    }

    @Test
    void deleteSpecific(){
        playerRepository.delete(playerRepository.findAll().get(0));
        assertTrue(playerRepository.findAll().size() == 1);
    }
}