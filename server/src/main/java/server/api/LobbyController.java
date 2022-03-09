package server.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import server.database.LobbyRepository;
import commons.Lobby;

@RestController
@RequestMapping("/api/lobby")
public class LobbyController {
    
    @Autowired
    private LobbyRepository repository;

    @PostMapping("/addLobby")
    @ResponseBody
    public Lobby addLobby(@RequestBody Lobby newLobby){
        repository.save(newLobby);
        return newLobby;
    }

    @GetMapping("/getAllLobbies")
    @ResponseBody
    public List<Lobby> getAllLobbies(){
        List<Lobby> lis = repository.findAll();
        return lis;
    }

    @GetMapping("/getLobby")
    @ResponseBody
    public Optional<Lobby> getLobby(@RequestParam Long lobbyId){
        Optional<Lobby> found = repository.findById(lobbyId);
        return found;
    }

    @GetMapping("/getLobbyByToken")
    @ResponseBody
    public Optional<Lobby> getLobbyByToken(@RequestParam String token){
        Optional<Lobby> found = repository.findByToken(token);
        return found;
    }

    @GetMapping("/clear")
    protected String clear(){
        repository.deleteAll();
        return "Cleared";
    }

    @GetMapping("/delete")
    public String deleteLobby(@RequestParam long id){
        Optional<Lobby> lobby = repository.findById(id);
        if(lobby.isEmpty()){
            return "Lobby not found";
        }

        repository.deleteById(id);
        return "Success";
    }
}
