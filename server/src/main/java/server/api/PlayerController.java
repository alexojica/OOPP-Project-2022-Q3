package server.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import server.database.PlayerRepository;
import commons.Player;

@RestController
@RequestMapping("api/player")
public class PlayerController {
    
    @Autowired
    private PlayerRepository repository;

    @PostMapping("/addPlayer")
    @ResponseBody
    public Player addPlayer(@RequestBody Player newPlayer){
        repository.save(newPlayer);
        return newPlayer;
    }

    @GetMapping("/getAllPlayers")
    @ResponseBody
    public List<Player> getAllPlayers(){
        List<Player> lis = repository.findAll();
        return lis;
    }

    @GetMapping("/getPlayer")
    @ResponseBody
    public Optional<Player> getPlayer(@RequestParam Long playerId){
        Optional<Player> found = repository.findById(playerId);
        return found;
    }


}
