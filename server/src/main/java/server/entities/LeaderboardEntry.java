package server.entities;

import javax.persistence.*;

@Entity
@Table(name = "Leadearboard")
public class LeaderboardEntry {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Column(name = "score")
    public int score;

    @Column(name = "name")
    public String name;

    @Column(name = "avatar")
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
