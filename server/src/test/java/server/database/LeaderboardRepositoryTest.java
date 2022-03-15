package server.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import server.entities.LeaderboardEntry;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class LeaderboardRepositoryTest {
    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @BeforeEach
    void initUseCase() {
        List<LeaderboardEntry> leaderboardEntries = Arrays.asList(
                new LeaderboardEntry(100, "andi", "somepath"), new LeaderboardEntry(90, "johnny", "somepath")
        );
        leaderboardRepository.saveAll(leaderboardEntries);
    }

    @AfterEach
    public void destroyAll(){
        leaderboardRepository.deleteAll();
    }

    @Test
    void saveAll_success() {
        List<LeaderboardEntry> leaderboardEntries = Arrays.asList(
                new LeaderboardEntry(30, "testPlayer", "someotherpath"),
                new LeaderboardEntry(10, "testPlayer2", "someavatarpath"),
                new LeaderboardEntry(99, "testPlayer3", "somepath")
        );
        Iterable<LeaderboardEntry> allCustomer = leaderboardRepository.saveAll(leaderboardEntries);

        AtomicInteger validIdFound = new AtomicInteger();
        allCustomer.forEach(leaderboardEntry -> {
            if(leaderboardEntry.id > 1){
                validIdFound.getAndIncrement();
            }
        });

        assertThat(validIdFound.intValue()).isEqualTo(3);
    }

    @Test
    void findAll_success() {
        List<LeaderboardEntry> allLeaderboardEntry = leaderboardRepository.findAll();
        assertThat(allLeaderboardEntry.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void findNoting(){
        leaderboardRepository.deleteAll();
        assertThat(leaderboardRepository.findAll().size() == 0);
    }

    @Test
    void findById(){
        long id = leaderboardRepository.findAll().get(0).id;
        LeaderboardEntry leaderboardEntry = leaderboardRepository.findById(id).get();
        assertTrue(leaderboardEntry != null);
        assertTrue(leaderboardEntry.getName().equals("andi"));
        id = leaderboardRepository.findAll().get(1).id;
        leaderboardEntry = leaderboardRepository.findById(id).get();
        assertTrue(leaderboardEntry != null);
        assertTrue(leaderboardEntry.getName().equals("johnny"));
        assertTrue(leaderboardEntry.getAvatarPath().equals("somepath"));
    }

    @Test
    void deleteSpecific(){
        leaderboardRepository.delete(leaderboardRepository.findAll().get(0));
        assertTrue(leaderboardRepository.findAll().size() == 1);
    }
}