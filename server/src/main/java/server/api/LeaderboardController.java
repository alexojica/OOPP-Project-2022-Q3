package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import server.database.LeaderboardRepository;
import commons.LeaderboardEntry;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    @Autowired
    private LeaderboardRepository repository;

    public LeaderboardController(LeaderboardRepository repository){
        this.repository = repository;
    }

    public LeaderboardController(){

    }

    /**
     * Find the top 10 entries in the leaderboard according to the score
     * @return a list of leaderboard entries that have the highest 10 scores
     */
    @GetMapping("/getTop10")
    public List<LeaderboardEntry> getTop10() {
        return repository.findTop10ByOrderByScoreDesc();
    }

    /**
     * Save the given leaderboard entry in the leaderboard repository
     * @param leaderboardEntry
     * @return the leaderboard entry object that was saved to the repository
     */
    @PostMapping("/persistScore")
    @ResponseBody
    @Transactional
    public LeaderboardEntry persistScore(@RequestBody LeaderboardEntry leaderboardEntry) {
        repository.save(leaderboardEntry);
        return leaderboardEntry;
    }

    @GetMapping("/getEntry")
    public Optional<LeaderboardEntry> getEntryByName(@RequestBody String name) {
        return repository.findByName(name);
    }

}
