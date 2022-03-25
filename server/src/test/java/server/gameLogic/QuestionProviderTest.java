package server.gameLogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.Mocks.TestActivitiesRepository;
import server.api.Mocks.TestQuestionRepository;
import server.database.LoadActivities;
import server.database.QuestionRepository;

class QuestionProviderTest {

    private QuestionProvider questionProvider;
    private TestActivitiesRepository testActivitiesRepository = new TestActivitiesRepository();
    private QuestionRepository questionRepository = new TestQuestionRepository();

    @BeforeEach
    void setup(){
        testActivitiesRepository = new TestActivitiesRepository();
        questionRepository = new TestQuestionRepository();
        new LoadActivities().saveIntoDatabase(testActivitiesRepository);
        System.out.println(testActivitiesRepository.findAll().size());
        questionProvider = new QuestionProvider();
    }

    @Test
    void getQuestion() {
//        questionProvider.getQuestion(10, "somelobby", 30);
//        assertEquals(questionRepository.count(), 1);
//        Question question = questionProvider.createNewQuestion();
        //assertEquals(question, questionProvider.getQuestion(question.idm ));
    }

    @Test
    void updatePointer() {
    }

    @Test
    void getRandomQuestionType() {
    }

    @Test
    void createNewQuestion() {
    }

    @Test
    void useQuestionAfter() {
    }

    @Test
    void getActivityPivot() {
    }

    @Test
    void getMultipleChoiceQuestionActivities() {
    }

    @Test
    void getAlternativeEnergyQuestionActivities() {
    }

    @Test
    void getEstimationQuestionActivity() {
    }
}