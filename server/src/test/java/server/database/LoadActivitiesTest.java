package server.database;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import commons.Activity;

import static org.mockito.Mockito.*;

class LoadActivitiesTest {

    private ActivitiesRepository mockRepository;
    private List<Activity> activities;

    @BeforeEach
    void setUp(){
        mockRepository = mock(ActivitiesRepository.class);
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Activity>> typeReference = new TypeReference<List<Activity>>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/activities.json");
        if(activities != null){
            try {
                activities = mapper.readValue(inputStream,typeReference);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void initDatabase() {
        if(activities != null) {
            // given
            LoadActivities loadActivities = new LoadActivities();

            // when
            when(mockRepository.saveAll(activities)).thenReturn(activities);
            loadActivities.saveIntoDatabase(mockRepository);

            // then
            verify(mockRepository).saveAll(activities);
        }
    }
}