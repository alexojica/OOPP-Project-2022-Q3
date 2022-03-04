package server.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import server.entities.Activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class LoadActivities {

    @Bean
    ApplicationRunner initDatabase(ActivitiesRepository repository){
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<List<Activity>> typeReference = new TypeReference<List<Activity>>(){};
                InputStream inputStream = TypeReference.class.getResourceAsStream("/activities.json");
                try {
                    List<Activity> users = mapper.readValue(inputStream,typeReference);
                    repository.saveAll(users);
                    System.out.println("Users Saved!");
                } catch (IOException e){
                    System.out.println("Unable to save users: " + e.getMessage());
                }
            }
        };
    }
}
