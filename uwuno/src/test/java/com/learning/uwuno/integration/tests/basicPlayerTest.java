package com.learning.uwuno.integration.tests;

import com.learning.uwuno.integration.testUtil;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class basicPlayerTest {
    private static String roomId;
    private final static String inputName = "test_player_name";

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        roomId = testUtil.createRoom("room_1").path("uid");
    }

    @AfterEach
    public void cleanUp() {
        testUtil.deleteRoom(roomId);
    }

    // POST valid Player
    @Test
    public void createValidPlayerInRoom200() throws FileNotFoundException {
        Response response = testUtil.createPlayer(inputName, roomId);

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("pid"), is(not(emptyString())));
        assertThat(response.path("name"), is(equalTo(inputName)));
        assertThat(response.path("cardList"), is(empty()));
    }

    // POST invalid Player with playerName = ""
    @Test
    public void createPlayerEmptyName400() throws IOException {
        Response response = testUtil.createPlayer("", roomId);
        assertThat(response.statusCode(), is(equalTo(400)));
    }

    // GET Player cards
    @Test
    public void getPlayerCards200() throws FileNotFoundException {
        String playerId = testUtil.createPlayer(inputName, roomId).path("pid");
        Response response = testUtil.getPlayerCards(roomId, playerId);
        assertThat(response.statusCode(), is(equalTo(200)));
    }

    // GET invalid Player cards - via invalid pid
    @Test
    public void getPlayerCardsInvalidPid404() {
        Response response = testUtil.getPlayerCards(roomId, "invalid_pid");
        assertThat(response.statusCode(), is(equalTo(404)));
    }

    // GET invalid Player cards - via invalid uid
    @Test
    public void getPlayerCardsInvalidUid404() throws FileNotFoundException {
        String playerId = testUtil.createPlayer(inputName, roomId).path("pid");
        Response response = testUtil.getPlayerCards("invalid_uid", playerId);
        assertThat(response.statusCode(), is(equalTo(404)));
    }

    // DELETE valid Player
    @Test
    public void deletePlayer200() throws FileNotFoundException {
        String playerId = testUtil.createPlayer(inputName, roomId).path("pid");
        Response response = testUtil.deletePlayer(roomId, playerId);
        assertThat(response.statusCode(), is(equalTo(200)));
    }

    // DELETE invalid Player - via invalid pid
    @Test
    public void deletePlayerInvalidPid404() {
        Response response = testUtil.deletePlayer(roomId, "invalid_pid");
        assertThat(response.statusCode(), is(equalTo(404)));
    }

    // DELETE invalid Player - via invalid uid
    @Test
    public void deletePlayerInvalidUid404() throws FileNotFoundException {
        String playerId = testUtil.createPlayer(inputName, roomId).path("pid");
        Response response = testUtil.deletePlayer("invalid_uid", playerId);
        assertThat(response.statusCode(), is(equalTo(404)));
    }

    // PUT valid name
    @Test
    public void putValidPlayerName200() throws FileNotFoundException {
        // Player
        String playerId = testUtil.createPlayer(inputName, roomId).path("pid");

        // Request
        String new_name = "new_name";
        Response response = testUtil.changePlayerName(roomId, playerId, new_name);

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("pid"), is(equalTo(playerId)));
        assertThat(response.path("name"), is(equalTo(new_name)));
    }

    // PUT invalid name
    @Test
    public void putInvalidPlayerName400() throws FileNotFoundException {
        String playerId = testUtil.createPlayer(inputName, roomId).path("pid");
        Response response = testUtil.changePlayerName(roomId, playerId, "");
        assertThat(response.statusCode(), is(equalTo(400)));
    }
}
