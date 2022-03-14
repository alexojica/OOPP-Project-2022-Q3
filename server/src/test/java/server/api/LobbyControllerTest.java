package server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Lobby;
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
import server.database.LobbyRepository;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class LobbyControllerTest {

    private LobbyController lobbyController = new LobbyController(new TestLobbyRepository());

    private Lobby publicLobby;
    private Lobby privateLobby;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LobbyRepository repository;

    @MockBean
    private ServletWebServerFactory servletWebServerFactory;

    @BeforeEach
    void setup(){
        publicLobby = new Lobby("public");
        privateLobby = new Lobby("private", 1);
        lobbyController.clear();
    }

    @Test
    void addLobby(){
        assertTrue(lobbyController.getAllLobbies().size() == 0);
        Lobby newLobby = new Lobby("private", 2);
        lobbyController.addLobby(newLobby);
        assertTrue(lobbyController.getAllLobbies().size() == 1);
        lobbyController.addLobby(publicLobby);
        assertTrue(lobbyController.getAllLobbies().size() == 2);
    }

    @Test
    void getAllLobbies(){
        Lobby newLobby = new Lobby("private", 2);
        lobbyController.addLobby(newLobby);
        lobbyController.addLobby(publicLobby);
        lobbyController.addLobby(privateLobby);
        assertTrue(lobbyController.getAllLobbies().contains(newLobby));
        assertTrue(lobbyController.getAllLobbies().contains(privateLobby));
        assertTrue(lobbyController.getAllLobbies().contains(publicLobby));
    }

    @Test
    void getLobbyByToken() {
        Lobby newLobby = new Lobby("private", 2);
        lobbyController.addLobby(newLobby);
        assertTrue(lobbyController.getLobbyByToken("private").get().equals(newLobby));
    }

    @Test
    void getConnectPermission() {
        assertTrue(lobbyController.getConnectPermission("private", "alex") == 1);
        Lobby newLobby = new Lobby("private", 2);
        lobbyController.addLobby(newLobby);
        assertTrue(lobbyController.getConnectPermission("private", "alex") == 2);
        newLobby.addPlayerToLobby(new Player("alex"));
        assertTrue(lobbyController.getConnectPermission("private", "alex") == 0);
        assertTrue(lobbyController.getConnectPermission("private", "testPlayer") == 2);
    }

    @Test
    void clear() {
        lobbyController.clear();
        assertTrue(lobbyController.getAllLobbies().isEmpty());
        Lobby newLobby = new Lobby("private", 2);
        lobbyController.addLobby(newLobby);
        lobbyController.clear();
        assertTrue(lobbyController.getAllLobbies().isEmpty());
    }

    @Test
    void addLobbyResponse() throws Exception{
        Lobby newLobby = new Lobby("private", 10);
        mockMvc.perform(post("/api/lobby/addLobby")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newLobby))
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("token").value("private"))
                .andExpect(jsonPath("hostId").value(10));
    }

    @Test
    void getAllLobbiesResponse() throws Exception{
        mockMvc.perform(get("/api/lobby/getAllLobbies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    void getLobbyByTokenResponse() throws Exception{
        mockMvc.perform(get("/api/lobby/getLobbyByToken")
                .param("token", "private"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
    }
}