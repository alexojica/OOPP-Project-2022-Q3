package server.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import server.database.PlayerRepository;
import server.entities.Player;

@RestController
public class PlayerController {
    
    @Autowired
    private PlayerRepository repository;

    @PostMapping("/api/addPlayer")
    @ResponseBody
    public String addPlayer(@RequestBody Player newPlayer){
        repository.save(newPlayer);
        return "Saved";
    }

    @GetMapping("/api/getAllPlayers")
    @ResponseBody
    public List<Player> getAllPlayers(){
        List<Player> lis = repository.findAll();
        return lis;
    }

    @GetMapping("/api/getPlayer")
    @ResponseBody
    public Optional<Player> getPlayer(@RequestParam Long playerId){
        Optional<Player> found = repository.findById(playerId);
        return found;
    }


}
