package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;
import static org.junit.jupiter.api.Assertions.*;

class ActivityTest {

    Activity act1, act2, act3;

    @BeforeEach
    void setUp(){
        act1 = new Activity("id1", "resources/image", "google query", 3526L, "http://source");
        act2 = new Activity("id3", "resources/image", "elephant food", 358L, "http://otherSource");
        act3 = new Activity("id1", "resources/image", "google query", 3526L, "http://source");
    }

    //the id is now auto-generated
//    @Test
//    void getId() {
//        assertEquals(act1.getId(), "id1");
//    }

    @Test
    void testConstructor(){
        assertNotNull(act1);
        assertNotNull(act2);
        assertNotNull(act3);
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

    @Test
    public void setEnergyConsumption() {
        Activity activity = act1;
        activity.setEnergyConsumption(15L);
        assertEquals(15L, activity.getEnergyConsumption());
    }
}
