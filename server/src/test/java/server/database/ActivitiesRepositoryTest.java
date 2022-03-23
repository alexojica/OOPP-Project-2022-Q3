package server.database;

import commons.Activity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class ActivitiesRepositoryTest {

    @Autowired
    private ActivitiesRepository activitiesRepository;

    @BeforeEach
    void initUseCase() {
        List<Activity> activities = Arrays.asList(
                new Activity("0", "somepath", "someTitle",
                        300L, "somesource"), new Activity("1", "somepath", "someTitle",
                        330L, "somesource")
        );
        activitiesRepository.saveAll(activities);
    }

    @AfterEach
    public void destroyAll(){
        activitiesRepository.deleteAll();
    }

    @Test
    void saveAll_success() {
        List<Activity> activities = Arrays.asList(
                new Activity("2", "someotherpath", "someTitle",
                        30L, "somesource"),
                new Activity("3", "somepath", "someTitle",
                        40L, "somesource"),
                new Activity("4", "somepath", "someTitle",
                        1000L, "somesource")
        );
        Iterable<Activity> allCustomer = activitiesRepository.saveAll(activities);

        AtomicInteger validIdFound = new AtomicInteger();
        allCustomer.forEach(activity -> {
            if(activity.getId() > 1){
                validIdFound.getAndIncrement();
            }
        });

        assertThat(validIdFound.intValue()).isEqualTo(3);
    }

    @Test
    void findAll_success() {
        List<Activity> allActivity = activitiesRepository.findAll();
        assertThat(allActivity.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void findNoting(){
        activitiesRepository.deleteAll();
        assertThat(activitiesRepository.findAll().size() == 0);
    }

    @Test
    void findById(){
        long id = activitiesRepository.findAll().get(0).getId();
        Activity activity = activitiesRepository.findById(id).get();
        assertTrue(activity != null);
        assertTrue(activity.getEnergyConsumption() == 300L);
        assertTrue(activity.getImagePath().equals("somepath"));
        id = activitiesRepository.findAll().get(1).getId();
        activity = activitiesRepository.findById(id).get();
        assertTrue(activity != null);
        assertTrue(activity.getEnergyConsumption() == 330L);
        assertTrue(activity.getImagePath().equals("somepath"));
    }

    @Test
    void findByEnergyConsumptionDesc() {
        List<Activity> activities = Arrays.asList(
                new Activity("2", "someotherpath", "someTitle",
                        30L, "somesource"),
                new Activity("3", "somepath", "someTitle",
                        40L, "somesource"),
                new Activity("4", "somepath", "someTitle",
                        1000L, "somesource")
        );
        activitiesRepository.saveAll(activities);
        activitiesRepository.save(new Activity("5", "somepath2", "title2", 1000L, "somesource2"));
        Activity activity = activitiesRepository.findByEnergyConsumptionDesc(1000L).get(0);
        assertTrue(activity.getImagePath().equals("somepath"));
//        assertTrue(activity.getId().equals(4));
        activity = activitiesRepository.findByEnergyConsumptionDesc(1000L).get(1);
        assertTrue(activity.getImagePath().equals("somepath2"));
//        assertTrue(activity.getId().equals("5"));
    }

    @Test
    void deleteSpecific(){
        activitiesRepository.delete(activitiesRepository.findByEnergyConsumptionDesc(1000L).get(0));
        assertTrue(activitiesRepository.findAll().size() == 1);
    }
}