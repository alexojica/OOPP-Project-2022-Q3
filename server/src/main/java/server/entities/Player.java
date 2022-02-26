package server.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

import java.util.ArrayList;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
@Table(name = "Player")
public class Player {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Column(name = "name")
    public String name;

    @Column(name = "avatar")
    public String avatar; //folder path

    @Column(name = "score")
    public Integer score;

    @Column(name = "jokers")
    public ArrayList<Boolean> usedPowerups;

    @Column(name = "lobbyId")
    public int lobbyId;

    @SuppressWarnings("unused")
    private Player() {
        // for object mapper
    }

    /**
     * Empty constructor with name only; no avatar path set
     * @param name
     */

    public Player(String name) {
        this.name = name;
        //initialise base score to 0
        this.score = 0;
        //initialise all power ups as unused
        this.usedPowerups = new ArrayList<>();
    }

    /**
     * Constructor with name and avatar path
     * @param name
     * @param avatarPath
     */
    public Player(String name, String avatarPath) {
        this.name = name;
        this.avatar = avatarPath;
        //initialise base score to 0
        this.score = 0;
        //initialise all power ups as unused
        this.usedPowerups = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public ArrayList<Boolean> getUsedPowerups() {
        return usedPowerups;
    }

    /**
     * Method that marks a user's power up (Joker) as used
     * @param id
     */
    public void usePowerup(int id)
    {
        usedPowerups.set(id,true);
    }

    public void setUsedPowerups(ArrayList<Boolean> usedPowerups) {
        this.usedPowerups = usedPowerups;
    }

    public int getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
