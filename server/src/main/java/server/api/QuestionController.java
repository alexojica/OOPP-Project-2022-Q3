package server.api;

import commons.Question;
import commons.WebsocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.gameLogic.QuestionProvider;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    private QuestionProvider questionProvider;

    @GetMapping("/getQuestion")
    @ResponseBody
    public Question getQuestion(@RequestParam Long pointer, @RequestParam String lastLobby) {
        return questionProvider.getQuestion(pointer, lastLobby, 30);
    }

    /**
     * Websocket mapping that sends back to client a new ResponseMessage containing a question.
     * @param message
     * @return
     */
    @MessageMapping("/nextQuestion")
    @SendTo("/topic/nextQuestion")
    public WebsocketMessage nextQuestion(WebsocketMessage message){
        Question generatedQuestion = questionProvider.getQuestion(message.getPointer(),
                message.getLobbyToken(), 30);
        System.out.println("Activities generated were " + generatedQuestion.getFoundActivities());
        return new WebsocketMessage(message.getCode(),
                message.getLobbyToken(), generatedQuestion);
    }
}
