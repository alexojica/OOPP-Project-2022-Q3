package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LongWrapperTest {

    private static LongWrapper longWrapper;
    private static LongWrapper longWrapper2;

    @BeforeEach
    private void setUp(){

        longWrapper = new LongWrapper(1L);
        longWrapper2 = new LongWrapper();
    }

    /*
    Tests the main constructor
     */
    @Test
    void testConstructorNotNull(){
        assertNotNull(longWrapper);
    }

    /*
    Tests the empty constructor
     */
    @Test
    void testConstructorNull(){
        assertNotNull(longWrapper2);
    }

    @Test
    void getId(){
        assertEquals(1L, longWrapper.getId());
    }

    @Test
    void setId(){
        LongWrapper longWrapper1 = longWrapper;
        longWrapper1.setId(3L);
        assertEquals(3L, longWrapper1.getId());
    }
}
