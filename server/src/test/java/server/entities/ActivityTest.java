package server.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActivityTest {

    Activity act1, act2, act3;

    @BeforeEach
    void setUp(){
        act1 = new Activity("id1", "resources/image", "google query", 3526L, "http://source");
        act2 = new Activity("id3", "resources/image", "elephant food", 358L, "http://otherSource");
        act3 = new Activity("id1", "resources/image", "google query", 3526L, "http://source");
    }

    @Test
    void getId() {
        assertEquals(act1.getId(), "id1");
    }

    @Test
    void getImagePath() {
        assertEquals(act1.getImagePath(), "resources/image");
    }

    @Test
    void getTitle() {
        assertEquals(act1.getTitle(), "google query");
    }

    @Test
    void getEnergyConsumption() {
        assertEquals(act1.getEnergyConsumption(), 3526L);
    }

    @Test
    void getSource() {
        assertEquals(act1.getSource(), "http://source");
    }

    @Test
    void testEquals() {
        assertEquals(act1, act3);
        assertNotEquals(act1, act2);
        assertNotEquals(null, act1);
    }
}