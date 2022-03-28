package server.api;

import commons.LeaderboardEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import server.api.Mocks.TestLeaderboardRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class LeaderboardControllerTest {

    private TestLeaderboardRepository repo;

    private LeaderboardController sut;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        repo = new TestLeaderboardRepository();
        sut = new LeaderboardController(repo);
    }


    @Test
    public void getTop10() {
        assertNotNull(sut.getTop10());
        LeaderboardEntry leaderboardEntry = new LeaderboardEntry(15, "someName", "someCode");
        LeaderboardEntry leaderboardEntry1 = new LeaderboardEntry(150, "someName", "someCode");
        LeaderboardEntry leaderboardEntry2 = new LeaderboardEntry(5, "someName", "someCode");
        repo.save(leaderboardEntry);
        repo.save(leaderboardEntry1);
        repo.save(leaderboardEntry2);
        List<LeaderboardEntry> leaderboardEntries = new ArrayList<LeaderboardEntry>();
        leaderboardEntries.add(leaderboardEntry);
        leaderboardEntries.add(leaderboardEntry1);
        leaderboardEntries.add(leaderboardEntry2);
        List<Integer> scores = new ArrayList<Integer>();
        List<LeaderboardEntry> leaderboardEntries1 = new ArrayList<LeaderboardEntry>();
        int i = 0;
        for(LeaderboardEntry l : leaderboardEntries){
            scores.add(l.getScore());
        }
        Collections.sort(scores, Collections.reverseOrder());
        for(LeaderboardEntry l : leaderboardEntries){
            if(scores.get(i) == l.getScore()){
                leaderboardEntries1.add(i, l);
            }
        }
        assertEquals(sut.getTop10(), leaderboardEntries1.stream().limit(10).collect(Collectors.toList()));
    }

    @Test
    void getByName(){
        LeaderboardEntry leaderboardEntry = new LeaderboardEntry(15, "someName", "someCode");
        repo.save(leaderboardEntry);
        assertEquals(Optional.of(leaderboardEntry), sut.getEntryByName("someName"));
    }

}

