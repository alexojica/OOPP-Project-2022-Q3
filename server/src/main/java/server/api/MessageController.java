package server.api;

import commons.WebsocketMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    /**
     * @param message received message that is to be redirected to all players subscribed to this endpoint
     * @return new websocketmessage to all players subscribed to this endpoint
     */
    @MessageMapping("/updateMessages")
    @SendTo("/topic/playerMessages")
    public WebsocketMessage updateMessages(WebsocketMessage message){
        return new WebsocketMessage(message.getQuestionType(), message.getMessage(), message.getLobbyToken());
    }
}
