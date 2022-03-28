package server.api;

import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import server.api.Mocks.TestActivitiesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class ActivityControllerTest {

    private TestActivitiesRepository repo;

    private ActivityController sut;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        repo = new TestActivitiesRepository();
        sut = new ActivityController(repo);
    }

    @Test
    void getActivityByWh() {
        assertNull(sut.getActivityByWh(100L));
        repo.save(new Activity("0", "somepath", "sometitle", 100L, "somesource"));
        assertEquals(sut.getActivityByWh(100L), new Activity("0", "somepath", "sometitle", 100L, "somesource"));
    }

    @Test
    void getRandomActivity() {
        assertNull(sut.getRandomActivity());
        repo.save(new Activity("0", "somepath", "sometitle", 100L, "somesource"));
        assertEquals(sut.getRandomActivity(), new Activity("0", "somepath", "sometitle", 100L, "somesource"));
    }

    @Test
    void getActivityByWhResponse() throws Exception{
        mockMvc.perform(get("/api/activity/getActivityByWh")
                .param("energyConsumption", String.valueOf(10L)))
                .andExpect(status().isOk());
    }

    @Test
    void getRandomActivityResponse() throws Exception{
        mockMvc.perform(get("/api/activity/getRandomActivity"))
                        .andExpect(status().isOk());
    }

    @Test
    public void getAll()
    {
        assertNotNull(sut.getAll());
        Activity activity = new Activity("0", "somepath", "sometitle", 100L, "somesource");
        repo.save(activity);
        List<Activity> activities = new ArrayList<Activity>();
        activities.add(activity);
        assertEquals(sut.getAll(), activities.stream().collect(Collectors.toList()));
    }

    @Test
    void getAllResponse() throws Exception{
        mockMvc.perform(get("/api/activity/getAll"))
                .andExpect(status().isOk());
    }
}
