package server.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class LeaderboardEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public int score;
    public String name;
    public String avatarPath;

    @SuppressWarnings("unused")
    private LeaderboardEntry() {
        // for object mapper
    }

    public LeaderboardEntry(int score, String name, String avatarPath) {
        this.score = score;
        this.name = name;
        this.avatarPath = avatarPath;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
}
