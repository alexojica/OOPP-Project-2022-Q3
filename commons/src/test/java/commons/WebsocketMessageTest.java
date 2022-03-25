package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class WebsocketMessageTest {

    WebsocketMessage wsm1, wsm2, wsm3;

    @BeforeEach
    void setUp(){
        //these are test websocketmessages for the communication of messages
        wsm1 = new WebsocketMessage("testMessage", "testToken");
        wsm2 = new WebsocketMessage("testMessage2", "testToken2");
        wsm3 = new WebsocketMessage("testMessage", "testToken");
    }

    @Test
    void getMessageTest(){
        assertEquals(wsm1.getMessage(), "testMessage");
        assertNotEquals(wsm1.getMessage(), "fakeTestMessage");
    }
    @Test
    void getLobbyTokenTest(){
        assertEquals(wsm1.getLobbyToken(), "testToken");
        assertNotEquals(wsm1.getLobbyToken(), "fakeTestToken");
    }

    @Test
    void testEquals() {
        assertEquals(wsm1, wsm3);
        assertNotEquals(wsm1, wsm2);
        assertNotEquals(null, wsm1);
    }
}