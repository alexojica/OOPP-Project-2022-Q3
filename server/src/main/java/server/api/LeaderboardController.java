package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.database.LeaderboardRepository;
import commons.LeaderboardEntry;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    @Autowired
    private LeaderboardRepository repository;

    @GetMapping("/getTop10")
    public List<LeaderboardEntry> getTop10() {
        return repository.findTop10ByOrderByScoreDesc();
    }

}
