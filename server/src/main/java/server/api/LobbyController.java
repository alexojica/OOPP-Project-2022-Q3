package server.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import server.database.LobbyRepository;
import server.entities.Lobby;

@RestController
public class LobbyController {
    
    @Autowired
    private LobbyRepository repository;

    @PostMapping("/api/addLobby")
    @ResponseBody
    public String addLobby(@RequestBody Lobby newLobby){
        repository.save(newLobby);
        return "Saved Lobby";
    }

    @GetMapping("/api/getAllLobbies")
    @ResponseBody
    public List<Lobby> getAllLobbies(){
        List<Lobby> lis = repository.findAll();
        return lis;
    }

    @GetMapping("/api/getLobby")
    @ResponseBody
    public Optional<Lobby> getLobby(@RequestParam Long lobbyId){
        Optional<Lobby> found = repository.findById(lobbyId);
        return found;
    }


}
