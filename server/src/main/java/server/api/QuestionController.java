package server.api;

import commons.Question;
import commons.WebsocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import server.gameLogic.QuestionProvider;

import java.util.HashMap;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    @Autowired
    private QuestionProvider questionProvider;

    private HashMap<String, Integer> lobbyToDifficultyMap;

    @GetMapping("/getQuestion")
    @ResponseBody
    public Question getQuestion(@RequestParam Long pointer, @RequestParam String lastLobby) {
        return questionProvider.getQuestion(pointer, lastLobby, 30);
    }

    @MessageMapping("/setDifficulty")
    public void setDifficulty(WebsocketMessage message)
    {
        String lobbyCode = message.getLobbyToken();
        Integer difficulty = message.getDifficultySetting();

        //don't need to check whether the lobby exists,
        //this step is already done in other configs
        if(lobbyToDifficultyMap == null) lobbyToDifficultyMap = new HashMap<>();
        lobbyToDifficultyMap.put(lobbyCode,difficulty);
    }

    /**
     * Websocket mapping that sends back to client a new ResponseMessage containing a question.
     * @param message
     * @return
     */
    @MessageMapping("/nextQuestion")
    @SendTo("/topic/nextQuestion")
    public WebsocketMessage nextQuestion(WebsocketMessage message){
        int difficulty = 30;
        //if not initialised <=> not making use of private lobbies settings
        if(lobbyToDifficultyMap != null && lobbyToDifficultyMap.containsKey(message.getLobbyToken()))
            difficulty = lobbyToDifficultyMap.get(message.getLobbyToken());

        Question generatedQuestion = questionProvider.getQuestion(message.getPointer(),
                message.getLobbyToken(), difficulty);
        System.out.println("Activities generated were " + generatedQuestion.getFoundActivities());
        return new WebsocketMessage(message.getCode(),
                message.getLobbyToken(), generatedQuestion);
    }
}
