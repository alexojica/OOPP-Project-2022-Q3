package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeaderboardEntryTest {

    @Test
    void getScore() {
        LeaderboardEntry leaderboardEntry = new LeaderboardEntry(100, "alex", "somecode");;
        assertEquals(leaderboardEntry.getScore(), 100);
    }

    @Test
    void setScore() {
        LeaderboardEntry leaderboardEntry = new LeaderboardEntry(100, "alex", "somecode");;
        assertEquals(leaderboardEntry.getScore(), 100);
        leaderboardEntry.setScore(50);
        assertEquals(leaderboardEntry.getScore(), 50);
    }

    @Test
    void getName() {
        LeaderboardEntry leaderboardEntry = new LeaderboardEntry(100, "alex", "somecode");;
        assertEquals(leaderboardEntry.getName(), "alex");
    }

    @Test
    void setName() {
        LeaderboardEntry leaderboardEntry = new LeaderboardEntry(100, "alex", "somecode");;
        assertEquals(leaderboardEntry.getName(), "alex");
        leaderboardEntry.setName("testPlayer");
        assertEquals(leaderboardEntry.getName(), "testPlayer");
    }

    @Test
    void getAvatarCode() {
        LeaderboardEntry leaderboardEntry = new LeaderboardEntry(100, "alex", "somecode");;
        assertEquals(leaderboardEntry.getAvatarCode(), "somecode");
    }

    @Test
    void setAvatarCode() {
        LeaderboardEntry leaderboardEntry = new LeaderboardEntry(100, "alex", "somecode");;
        assertEquals(leaderboardEntry.getAvatarCode(), "somecode");
        leaderboardEntry.setAvatarCode("someOtherCode");
        assertEquals(leaderboardEntry.getAvatarCode(), "someOtherCode");
    }
}