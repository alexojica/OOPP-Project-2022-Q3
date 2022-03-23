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

    /**
     * Given the Player object, Player is saved to the repository
     * @param newPlayer
     * @return the new player that was saved to the repo
     * @throws Exception
     */
    @PostMapping("/addPlayer")
    @ResponseBody
    public Player addPlayer(@RequestBody Player newPlayer) throws Exception{
        if(newPlayer == null)
            throw new WrongParameterException();
        repository.save(newPlayer);
        return newPlayer;
    }

    /**
     * Find all the players that are stored in the database
     * @return full list of players that are in the repo
     */
    @GetMapping("/getAllPlayers")
    @ResponseBody
    public List<Player> getAllPlayers(){
        List<Player> lis = repository.findAll();
        return lis;
    }

    /**
     * Given the id of a Player, find that player
     * @param playerId
     * @return if the player with that id was found, return it
     */
    @GetMapping("/getPlayer")
    @ResponseBody
    public Optional<Player> getPlayer(@RequestParam Long playerId){
        Optional<Player> found = repository.findById(playerId);
        return found;
    }

    /**
     * Deletes all the player objects in the repository
     * @return a string that implies all the players were deleted
     */
    @GetMapping("/clear")
    protected String clear(){
        repository.deleteAll();
        return "Cleared";
    }

    /**
     * Given the id of the player object, delete that player from the repository
     * @param id
     * @return a string that whether the deletion was successful or not
     */
    @GetMapping("/delete")
    public String deletePlayer(@RequestParam long id){
        Optional<Player> player = repository.findById(id);
        if(player.isEmpty()){
            return "Player not found";
        }

        repository.deleteById(id);
        return "Success";
    }

    /**
     * Given the player object with the new score, it edits the score of that player
     * @param player
     * @return the Player with the updated score
     * @throws WrongParameterException
     */
    @PutMapping("/updateScore")
    public Player updateScore(@RequestBody Player player) throws WrongParameterException{
        if(player==null){
            throw new WrongParameterException();
        }
        repository.updateScore(player.getScore(), player.getId());
        return player;
    }
}
