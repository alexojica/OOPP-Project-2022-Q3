package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import commons.Player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlayerTest {

    private static Player player;
    private static Player player2;

    @BeforeEach
    private void createPlayer(){
        player = new Player("arda");
        player2 = new Player();
    }

    /*
    Tests the main constructor
     */
    @Test
    void testConstructorNotNull(){
        assertNotNull(player);
    }

    /*
    Tests the empty constructor
     */
    @Test
    void testConstructorNull(){
        assertNotNull(player2);
    }

    @Test
    void getName(){
        assertEquals("arda", player.getName());
    }

    @Test
    void getScore(){
        assertEquals(0, player.getScore());
    }

    @Test
    void setName(){
        Player player1 = player;
        player1.setName("simi");
        assertEquals("simi", player1.getName());
    }

    @Test
    void setScore(){
        Player player1 = player;
        player1.setScore(5);
        assertEquals(5, player1.getScore());
    }

    /*
    @Test
    void setAndGetLobbyId(){
        Player player1 = player;
        player1.setLobbyId(5);
        assertEquals(5, player1.getLobbyId());
    }
    */
}
