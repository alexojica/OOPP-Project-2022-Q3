package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Player;
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
import server.Exceptions.WrongParameterException;
import server.database.PlayerRepository;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class PlayerControllerTest {
    private PlayerController playerController = new PlayerController(new TestPlayerRepository());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlayerRepository repository;

    @MockBean
    private ServletWebServerFactory servletWebServerFactory;

    @BeforeEach
    private void setup(){
        playerController = new PlayerController(new TestPlayerRepository());
    }

    @Test
    void addPlayer() throws Exception {
        Player player = new Player("alex");
        assertEquals(playerController.addPlayer(player), player);
        assertEquals(playerController.addPlayer(player).getId(), player.getId());
        assertTrue(playerController.getAllPlayers().contains(player));
        assertEquals(playerController.getPlayer(player.id).get(), player);
        assertThrows(WrongParameterException.class, () -> playerController.addPlayer(null));
    }

    @Test
    void getAllPlayers() throws Exception {
        assertTrue(playerController.getAllPlayers().isEmpty());
        Player player = new Player("alex");
        playerController.addPlayer(player);
        List<Player> players = new ArrayList<Player>();
        players.add(player);
        assertEquals(playerController.getAllPlayers(), players);
        player = new Player("testPlayer", "someAvatarPath");
        players.add(player);
        playerController.addPlayer(player);
        assertEquals(playerController.getAllPlayers(), players);
    }

    @Test
    void getPlayer() throws Exception {
        assertTrue(!playerController.getPlayer(10L).isPresent());
        Player player = new Player("alex");
        playerController.addPlayer(player);
        assertEquals(playerController.getPlayer(player.id).get(), player);
        player = new Player("testPlayer", "somePath");
        playerController.addPlayer(player);
    }

    @Test
    void clear() throws Exception {
        assertEquals(playerController.clear(), "Cleared");
        Player player = new Player("alex");
        playerController.addPlayer(player);
        player = new Player("testPlayer", "somePath");
        playerController.addPlayer(player);
        assertTrue(playerController.clear().equals("Cleared"));
        assertTrue(playerController.getAllPlayers().isEmpty());
    }

    @Test
    void deletePlayer() throws Exception {
        assertEquals(playerController.deletePlayer(10L), "Player not found");
        Player player = new Player("alex");
        playerController.addPlayer(player);
        player = new Player("testPlayer", "somePath");
        playerController.addPlayer(player);
        assertEquals(playerController.deletePlayer(player.id), "Success");
        assertTrue(playerController.getAllPlayers().size() == 1);
    }

    @Test
    void addPlayerResponse() throws Exception{
        Player player = new Player("alex");
        mockMvc.perform(post("/api/player/addPlayer")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(player))
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("name").value("alex"));

        mockMvc.perform(post("/api/player/addPlayer")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(null))
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllPlayersResponse() throws Exception{
        mockMvc.perform(get("/api/player/getAllPlayers"))
                .andExpect(status().isOk());
    }

    @Test
    void getPlayerResponse() throws Exception{
        mockMvc.perform(get("/api/player/getPlayer").param("playerId", "0"))
                .andExpect(status().isOk());
    }

    @Test
    void clearResponse() throws Exception{
        mockMvc.perform(get("/api/player/clear"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteResponse() throws Exception{
        mockMvc.perform(get("/api/player/delete")
                .param("id", "0"))
                .andExpect(status().isOk());
    }

    @Test
    void wrongPathsResponse() throws Exception{
        mockMvc.perform(get("/api/player/get"))
                .andExpect(status().isNotFound());
    }
}