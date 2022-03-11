package server.api;

import commons.Activity;
import commons.Question;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import server.database.ActivitiesRepository;
import server.database.QuestionRepository;

import java.util.*;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    private ActivitiesRepository activitiesRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/getQuestion")
    @ResponseBody
    public Question getQuestion(@RequestParam Long pointer, @RequestParam String lastLobby) {
        Optional<Question> foundQuestion = questionRepository.findByPointer(pointer);

        Random random = new Random();
        long newPointer = random.nextInt((int) questionRepository.count() * 2 + 1) + 1;
        if(pointer == newPointer) newPointer++;

        if(foundQuestion.isEmpty())
        {
            int difficulty = 30; //percentage
            Question question;

            int idx = random.nextInt(42);

            List<Activity> activities = activitiesRepository.findAll();
            Activity activityPivot;

            activityPivot = activities.get(random.nextInt(activities.size()));

            switch (idx % 3){
                case 0:

                    Activity activityLeft = (activitiesRepository.findByEnergyConsumptionDesc(activityPivot.getEnergyConsumption() * (100 - difficulty) / 100)).get(0);
                    Activity activityRight = (activitiesRepository.findByEnergyConsumptionDesc(activityPivot.getEnergyConsumption() * (100 + difficulty) / 100)).get(0);
                    activities.clear();
                    activities.add(activityLeft);
                    activities.add(activityPivot);
                    activities.add(activityRight);

                    break;
                case 1:

                    Long current = activityPivot.getEnergyConsumption();
                    List<Activity> randList = new ArrayList<>();
                    for(int i = 0; i < 6;i++)
                    {
                        Activity a = new Activity();
                        Long res = (long)(current * (100 - (i - 2.5) * difficulty) / 100);
                        a.setEnergyConsumption(res);
                        randList.add(a);
                    }
                    Collections.shuffle(randList);
                    activities.clear();

                    activities.add(activityPivot);
                    //activities.add(randList.get(0));
                    //activities.add(randList.get(1));

                    break;
                case 2:

                    long small = activityPivot.getEnergyConsumption() * (100 - difficulty) / 100;
                    long big = activityPivot.getEnergyConsumption() * (100 + difficulty) / 100;
                    List<Activity> foundActivities = activitiesRepository.findActivitiesInRange(small,big).get();
                    if(foundActivities.size() <= 4)
                    {
                        foundActivities = activitiesRepository.findActivitiesInRange(small / 2,big * 2).get();
                    }
                    foundActivities.remove(activityPivot);
                    Collections.shuffle(foundActivities);


                    activities.clear();

                    activities.add(activityPivot);
                    activities.add(foundActivities.get(0));
                    activities.add(foundActivities.get(1));
                    activities.add(foundActivities.get(2));

                    break;
                default:break;
            }

            question = new Question(pointer,idx % 3,newPointer,activities,lastLobby);
            System.out.println(question);
            questionRepository.save(question);
            return question;
        }
        else
        {
            Question q = foundQuestion.get();
            if(!Objects.equals(q.getLastLobbyToken(), lastLobby)) {
                //coming from a new lobby
                System.out.println(q.getLastLobbyToken() + " " + lastLobby);
                q.setPointer(newPointer);
                q.setLastLobbyToken(lastLobby);
                questionRepository.save(q);
            }
            return q;
        }
    }

}
