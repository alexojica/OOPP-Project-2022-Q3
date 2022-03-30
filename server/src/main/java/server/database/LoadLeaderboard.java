package server.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import commons.LeaderboardEntry;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class LoadLeaderboard {

    @Bean
    ApplicationRunner fillDatabase(LeaderboardRepository repository){
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                saveIntoDatabase(repository);
            }
        };
    }

    public void saveIntoDatabase(LeaderboardRepository repository){
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<LeaderboardEntry>> typeReference = new TypeReference<List<LeaderboardEntry>>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/leaderboardentries.json");
        if(inputStream == null){
            return;
        }
        try {
            List<LeaderboardEntry> leaderboardEntries = mapper.readValue(inputStream,typeReference);
            repository.saveAll(leaderboardEntries);
            System.out.println("LeaderboardEntries Saved!");
        } catch (IOException e){
            System.out.println("Unable to save leaderboard: " + e.getMessage());
        }
    }
}
