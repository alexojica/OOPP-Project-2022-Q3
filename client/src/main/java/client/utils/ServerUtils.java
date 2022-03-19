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

import commons.*;
import constants.ConnectionStatusCodes;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

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

    public ConnectionStatusCodes startLobby(String token) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lobby/startLobby") //
                .queryParam("token", token)//
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<ConnectionStatusCodes>(){});
    }

    private StompSession session = connect("ws://localhost:8080/websocket");

    private StompSession connect(String url){
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stomp = new WebSocketStompClient(client);

        stomp.setMessageConverter(new MappingJackson2MessageConverter());

        try{
            return stomp.connect(url, new StompSessionHandlerAdapter() {
                @Override
                public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                    System.out.println("connected");
                }
            }).get();
        }catch (ExecutionException e) {
            Thread.currentThread().interrupt();
            System.out.println("not working");
        } catch (InterruptedException e) {
            System.out.println("not working");
            throw new RuntimeException(e);
        } throw new IllegalStateException();
    }


    public void registerForMessages(String dest, Consumer<ResponseMessage> consumer){
        System.out.println(session.isConnected());
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ResponseMessage.class;
            }

            //@SuppressWarnings("unchecked")
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("receiving message");
                consumer.accept((ResponseMessage) payload);
            }

        });

        //session.send("/app/test", "Test");
    }

    public Lobby addMeToLobby(String token, Player player){
        ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/lobby/addMeToLobby") //
                .queryParam("token", token)//
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON)
                .post(Entity.entity(player, APPLICATION_JSON), Player.class);
        return getLobbyByToken(token);
    }

    public void send(String dest, Object o){
        session.send(dest, o);
    }
}