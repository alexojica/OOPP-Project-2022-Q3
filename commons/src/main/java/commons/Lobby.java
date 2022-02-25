package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Lobby {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public Integer token;
    public Boolean isPublic;
    public Integer hostId;
    public List<Player> players;

    @SuppressWarnings("unused")
    private Lobby() {
        // for object mapper
    }

    /**
     * Instantiates the public lobby with empty list of players
     * hostId remains null, as there is no host of a public lobby
     * @param token
     */
    public Lobby(Integer token) {
        this.token = token;
        this.isPublic = true;
        this.players = new ArrayList<>();
        this.hostId = null;
    }

    /**
     * Instantiates a private lobby given a token, and a host id
     * @param token
     * @param hostId
     */
    public Lobby(Integer token, Integer hostId)
    {
        this.token = token;
        this.hostId = hostId;
        this.players = new ArrayList<>();
        this.isPublic = false;
    }

    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }

    public Integer getHostId() {
        return hostId;
    }

    public void setHostId(Integer hostId) {
        this.hostId = hostId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
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
