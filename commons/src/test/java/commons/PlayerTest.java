package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private static Player player;
    private static Player player1;
    private static Player player2;

    @BeforeEach
    private void createPlayer(){
        player = new Player("arda");
        player1 = new Player("someName", "somePath");
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

    @Test
    public void getAvatar() {
        assertEquals("somePath", player1.getAvatar());
    }

    @Test
    public void setAvatar() {
        player1.setAvatar("someOtherPath");
        assertEquals("someOtherPath", player1.getAvatar());
    }

    @Test
    public void setAvatarCode() {
        player1.setAvatarCode("someOtherPath");
        assertEquals("someOtherPath", player1.getAvatarCode());
    }

    @Test
    void testEquals(){
        assertNotEquals(player, player1);
        Player player3 = player;
        assertEquals(player3, player);
    }
}
