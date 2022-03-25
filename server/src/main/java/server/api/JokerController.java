package server.api;

import commons.WebsocketMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/joker")
public class JokerController {

    @MessageMapping("/updateJoker")
    @SendTo("/topic/updateJoker")
    public WebsocketMessage updateJokerToAllLobby(WebsocketMessage message){
        System.out.println("redirecting joker");
        return new WebsocketMessage(message.getJokerType(), message.getLobbyToken(), message.getSenderName());
    }
}