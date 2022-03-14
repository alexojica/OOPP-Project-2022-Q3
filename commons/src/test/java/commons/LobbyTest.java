package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import commons.Lobby;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LobbyTest {

    private static Lobby lobby;
    private static Lobby lobby2;

    @BeforeEach
    private void createLobby(){
        lobby = new Lobby("arda");
        lobby2 = new Lobby();
    }

    /*
    Tests the main constructor
     */
    @Test
    void testConstructorNotNull(){
        assertNotNull(lobby);
    }

    /*
    Tests the empty constructor
     */
    @Test
    void testConstructorNull(){
        assertNotNull(lobby2);
    }

    @Test
    void getToken(){
        assertEquals("arda", lobby.getToken());
    }

    @Test
    void getHostId(){
        assertEquals(null, lobby.getHostId());
    }

    @Test
    void setToken(){
        Lobby lobby1 = lobby;
        lobby1.setToken("simi");
        assertEquals("simi", lobby1.getToken());
    }

    @Test
    void setAndGetHostId(){
        Lobby lobby1 = lobby;
        lobby1.setHostId(5);
        assertEquals(5, lobby1.getHostId());
    }
}
