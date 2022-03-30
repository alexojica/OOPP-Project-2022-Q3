package server.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import commons.Activity;

@Configuration
public class LoadActivities {

    @Bean
    ApplicationRunner initDatabase(ActivitiesRepository repository){
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                //if this returns something there should be activities in the db so we dont save
                if(repository.findAll()!=null){
                    System.out.println("Activities already in repo");
                    return;
                }
                saveIntoDatabase(repository);
            }
        };
    }

    public void saveIntoDatabase(ActivitiesRepository repository){
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Activity>> typeReference = new TypeReference<List<Activity>>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/activities.json");
        if(inputStream == null){
            return;
        }
        try {
            List<Activity> activities = mapper.readValue(inputStream,typeReference);
            repository.saveAll(activities);
            System.out.println("Activities Saved!");
        } catch (IOException e){
            System.out.println("Unable to save activities: " + e.getMessage());
        }
    }
}
