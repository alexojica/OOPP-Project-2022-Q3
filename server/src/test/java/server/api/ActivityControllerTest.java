package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import server.database.ActivitiesRepository;
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

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ActivitiesRepository repository;

    @MockBean
    private ServletWebServerFactory servletWebServerFactory;

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
}