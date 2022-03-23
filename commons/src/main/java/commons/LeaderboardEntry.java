package commons;

import javax.persistence.*;

@Entity
@Table(name = "Leaderboard")
public class LeaderboardEntry {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Column(name = "score")
    public int score;

    @Column(name = "name")
    public String name;

    @Column(name = "avatarCode")
    public String avatarCode;

    @SuppressWarnings("unused")
    private LeaderboardEntry() {
        // for object mapper
    }

    //For now I removed avatarPath from the constructor because I dont think it is needed anyway
    //as we'll generate the avatar from the avatarCode when loading the leaderboard
    public LeaderboardEntry(int score, String name, String avatarCode) {
        this.score = score;
        this.name = name;
        this.avatarCode = avatarCode;
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

    public String getAvatarCode() {
        return avatarCode;
    }

    public void setAvatarCode(String avatarCode) {
        this.avatarCode = avatarCode;
    }
}
