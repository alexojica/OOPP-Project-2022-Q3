package server.api;

import commons.Question;
import commons.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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

    @MessageMapping("/nextQuestion")
    @SendTo("/topic/nextQuestion")
    public ResponseMessage nextQuestion(ResponseMessage message){
        QuestionProvider questionProvider = new QuestionProvider(activitiesRepository, questionRepository);
        return new ResponseMessage(message.getCode(),
                message.getLobbyToken(), questionProvider.getQuestion(message.getPointer(),
                message.getLobbyToken(), 30));
    }
}
