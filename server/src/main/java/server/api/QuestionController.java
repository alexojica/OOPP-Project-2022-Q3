package server.api;

import commons.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.database.ActivitiesRepository;
import server.database.QuestionRepository;
import server.gameLogic.QuestionProvider;

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
        QuestionProvider questionProvider = new QuestionProvider(activitiesRepository, questionRepository);
        return questionProvider.getQuestion(pointer, lastLobby, 30);
    }

}
