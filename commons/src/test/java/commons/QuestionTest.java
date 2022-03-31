package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static constants.QuestionTypes.ESTIMATION_QUESTION;

import static constants.QuestionTypes.MULTIPLE_CHOICE_QUESTION;
import static org.junit.jupiter.api.Assertions.*;

public class QuestionTest {

    private static Question question;
    private static Question question2;
    private static Set<Long> activities = new HashSet<>();

    @BeforeEach
    private void setUp(){
        activities.add(1L);
        question = new Question(1L, MULTIPLE_CHOICE_QUESTION, 1L, activities, "someToken");
        question2= new Question();
    }

    @Test
    public void getType() {
        assertEquals(MULTIPLE_CHOICE_QUESTION, question.getType());
    }

    @Test
    public void setType() {
        Question question1 = question;
        question1.setType(ESTIMATION_QUESTION);
        assertEquals(ESTIMATION_QUESTION, question1.getType());
    }

    @Test
    public void getPointer() {
        assertEquals(1L, question.getPointer());
    }

    @Test
    public void getLastLobbyToken() {
        assertEquals("someToken", question.getLastLobbyToken());
    }

    @Test
    public void setLastLobbyToken() {
        Question question1 = question;
        question1.setLastLobbyToken("ESTIMATION_QUESTION");
        assertEquals("ESTIMATION_QUESTION", question1.getLastLobbyToken());
    }

    @Test
    public void setPointer() {
        Question question1 = question;
        question1.setPointer(2L);
        assertEquals(2L, question1.getPointer());
    }

    @Test
    public void getFoundActivities() {
        assertEquals(activities.stream().collect(Collectors.toList()), question.getFoundActivities());
    }

    @Test
    void testEquals() {
        Question question1 = question;
        assertEquals(question1, question);
    }

    @Test
    void testNotEquals() {
        assertNotEquals(question2, question);
    }

    /*
        Tests the main constructor
         */
    @Test
    void testConstructorNotNull(){
        assertNotNull(question);
    }

    /*
    Tests the empty constructor
     */
    @Test
    void testConstructorNull(){
        assertNotNull(question2);
    }

}
