package server.api;

import commons.WebsocketMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("api/joker")
public class JokerController {

    @PostMapping("/updateJoker")
    @MessageMapping("/updateJoker")
    @SendTo("/topic/updateJoker")
    public WebsocketMessage updateJokerToAllLobby(WebsocketMessage message){
        return new WebsocketMessage(message.getJokerType(), message.getLobbyToken());
    }
}