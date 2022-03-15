package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeaderboardEntryTest {

    @Test
    void getScore() {
        LeaderboardEntry leaderboardEntry = new LeaderboardEntry(100, "alex", "somepath");;
        assertEquals(leaderboardEntry.getScore(), 100);
    }

    @Test
    void setScore() {
        LeaderboardEntry leaderboardEntry = new LeaderboardEntry(100, "alex", "somepath");;
        assertEquals(leaderboardEntry.getScore(), 100);
        leaderboardEntry.setScore(50);
        assertEquals(leaderboardEntry.getScore(), 50);
    }

    @Test
    void getName() {
        LeaderboardEntry leaderboardEntry = new LeaderboardEntry(100, "alex", "somepath");;
        assertEquals(leaderboardEntry.getName(), "alex");
    }

    @Test
    void setName() {
        LeaderboardEntry leaderboardEntry = new LeaderboardEntry(100, "alex", "somepath");;
        assertEquals(leaderboardEntry.getName(), "alex");
        leaderboardEntry.setName("testPlayer");
        assertEquals(leaderboardEntry.getName(), "testPlayer");
    }

    @Test
    void getAvatarPath() {
        LeaderboardEntry leaderboardEntry = new LeaderboardEntry(100, "alex", "somepath");;
        assertEquals(leaderboardEntry.getAvatarPath(), "somepath");
    }

    @Test
    void setAvatarPath() {
        LeaderboardEntry leaderboardEntry = new LeaderboardEntry(100, "alex", "somepath");;
        assertEquals(leaderboardEntry.getAvatarPath(), "somepath");
        leaderboardEntry.setAvatarPath("someOtherPath");
        assertEquals(leaderboardEntry.getAvatarPath(), "someOtherPath");
    }
}