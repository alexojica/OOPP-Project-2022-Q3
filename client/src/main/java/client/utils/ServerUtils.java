/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import commons.Activity;
import commons.Lobby;
import commons.Player;
import commons.Question;
import constants.ConnectionStatusCodes;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class ServerUtils {

    String SERVER = "http://localhost:8080/";

    public List<Player> getPlayers() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/player/getAllPlayers") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Player>>() {});
    }


    public Player addPlayer(Player player) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/player/addPlayer") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(player, APPLICATION_JSON), Player.class);
    }

    public Lobby addLobby(Lobby lobby) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lobby/addLobby") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(lobby, APPLICATION_JSON), Lobby.class);
    }

    public List<Lobby> getAllLobbies() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lobby/getAllLobbies") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Lobby>>() {});
    }

    public Lobby getLobbyByToken(String token){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lobby/getLobbyByToken")
                .queryParam("token", token)//
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Lobby>() {});
    }

    public ConnectionStatusCodes getConnectPermission(String token, String playerUsername){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lobby/getConnectPermission") //
                .queryParam("token", token)//
                .queryParam("playerUsername", playerUsername)
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<ConnectionStatusCodes>(){});
    }

    public Activity getRandomActivity() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/activity/getRandomActivity")
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Activity>() {});
    }

    public Question getQuestion(long pointer, String lastLobby){
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/question/getQuestion") //
                .queryParam("pointer", pointer)//
                .queryParam("lastLobby", lastLobby)//
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Question>(){}); 
    }

    public Lobby startLobby(String token) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lobby/startLobby") //
                .queryParam("token", token)//
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Lobby>() {});
    }
}