package server.gameLogic;

import commons.Activity;
import commons.Question;
import constants.QuestionTypes;
import server.database.ActivitiesRepository;
import server.database.QuestionRepository;

import java.util.*;

public class QuestionProvider {
    private ActivitiesRepository activitiesRepository;
    private QuestionRepository questionRepository;

    private long pointer;
    private long newPointer;
    private int difficulty;
    private String lastLobby;
    private Random random;

    public QuestionProvider(ActivitiesRepository activitiesRepository, QuestionRepository questionRepository) {
        this.activitiesRepository = activitiesRepository;
        this.questionRepository = questionRepository;
        random = new Random();
    }

    /**
     * The important method of this class, it gets the next linked question or creates a new one
     * @return the next or newly created question
     */
    public Question getQuestion(long pointer, String lastLobby, int difficulty) {
        this.pointer = pointer;
        this.lastLobby = lastLobby;
        this.difficulty = difficulty;
        Optional<Question> foundQuestion = questionRepository.findById(pointer);

        updatePointer();

        if (foundQuestion.isEmpty()) {
            System.out.println("New question created");
            return createNewQuestion();
        } else {
            return useQuestionAfter(foundQuestion);
        }
    }

    public void updatePointer() {
        newPointer = Math.abs(random.nextInt((int) (questionRepository.count()+1) * 21) + 1);
        System.out.println("[OLD POINTER] " + pointer + ", " + "[NEW POINTER]" + newPointer);
        if (pointer == newPointer) newPointer++;
    }

    public QuestionTypes getRandomQuestionType() {
        switch (Math.abs(random.nextInt(2))) {
            case 0:
                return QuestionTypes.MULTIPLE_CHOICE_QUESTION;
            case 1:
                return QuestionTypes.ESTIMATION_QUESTION;
            case 2:
                return QuestionTypes.ENERGY_ALTERNATIVE_QUESTION;
            default:
                return null;
        }
    }

    public Question createNewQuestion() {
        QuestionTypes questionType = getRandomQuestionType();
        List<Activity> activities;
        Activity activityPivot = getActivityPivot();

        switch (questionType) {
            case MULTIPLE_CHOICE_QUESTION:
                activities = getMultipleChoiceQuestionActivities(activityPivot);
                break;
            case ESTIMATION_QUESTION:
                activities = getEstimationQuestionActivity(activityPivot);
                break;
            case ENERGY_ALTERNATIVE_QUESTION:
                activities = getAlternativeEnergyQuestionActivities(activityPivot);
                break;
            default:
                activities = null;
        }

        Question question = new Question(pointer, questionType, newPointer, activities, lastLobby);
        questionRepository.save(question);
        System.out.println(question);
        return question;
    }


    public Question useQuestionAfter(Optional<Question> foundQuestion) {
        Question q = foundQuestion.get();
        if (!Objects.equals(q.getLastLobbyToken(), lastLobby)) {
            q.setPointer(newPointer);
            q.setLastLobbyToken(lastLobby);
            System.out.println("A question has been reused");
            questionRepository.save(q);
        }
        return q;
    }

    public Activity getActivityPivot() {
        List<Activity> activities = activitiesRepository.findAll();
        return activities.get(random.nextInt(activities.size()));
    }

    public List<Activity> getMultipleChoiceQuestionActivities(Activity activityPivot) {
        List<Activity> activities = new ArrayList<>();
        Activity activityLeft = (activitiesRepository.findByEnergyConsumptionDesc(
                activityPivot.getEnergyConsumption() * (100 - difficulty) / 100)).get(0);
        Activity activityRight = (activitiesRepository.findByEnergyConsumptionDesc(
                activityPivot.getEnergyConsumption() * (100 + difficulty) / 100)).get(0);

        activities.add(activityLeft);
        activities.add(activityPivot);
        activities.add(activityRight);
        return activities;
    }

    public List<Activity> getAlternativeEnergyQuestionActivities(Activity activityPivot) {
        long small = activityPivot.getEnergyConsumption() * (100 - difficulty) / 100;
        long big = activityPivot.getEnergyConsumption() * (100 + difficulty) / 100;
        List<Activity> foundActivities = activitiesRepository.findActivitiesInRange(small, big).get();
        if (foundActivities.size() <= 4) {
            foundActivities = activitiesRepository.findActivitiesInRange(small / 2, big * 2).get();
        }
        foundActivities.remove(activityPivot);
        Collections.shuffle(foundActivities);
        List<Activity> activities = new ArrayList<>();
        activities.add(activityPivot);
        activities.add(foundActivities.get(0));
        activities.add(foundActivities.get(1));
        activities.add(foundActivities.get(2));
        return activities;
    }

    public List<Activity> getEstimationQuestionActivity(Activity activityPivot) {
        List<Activity> activities = new ArrayList<>();
        activities.add(activityPivot);
        return activities;
    }
}
