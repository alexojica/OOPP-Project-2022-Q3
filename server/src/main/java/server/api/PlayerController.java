package server.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import server.Exceptions.WrongParameterException;
import server.database.PlayerRepository;
import commons.Player;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
    
    @Autowired
    private PlayerRepository repository;

    public PlayerController(PlayerRepository repository){
        this.repository = repository;
    }

    public PlayerController(){

    }

    @PostMapping("/addPlayer")
    @ResponseBody
    public Player addPlayer(@RequestBody Player newPlayer) throws Exception{
        if(newPlayer == null)
            throw new WrongParameterException();
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

    @GetMapping("/clear")
    protected String clear(){
        repository.deleteAll();
        return "Cleared";
    }

    @GetMapping("/delete")
    public String deletePlayer(@RequestParam long id){
        Optional<Player> player = repository.findById(id);
        if(player.isEmpty()){
            return "Player not found";
        }

        repository.deleteById(id);
        return "Success";
    }

    @PutMapping("/updateScore")
    public Player updateScore(@RequestBody Player player) throws WrongParameterException{
        if(player==null){
            throw new WrongParameterException();
        }
        repository.updateScore(player.getScore(), player.getId());
        return player;
    }
}
