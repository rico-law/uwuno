package com.learning.uwuno.integration.tests;

import com.learning.uwuno.integration.testUtil;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class basicRoomTest {
    private final static ArrayList<String> runningRoomIds = new ArrayList<>();
    private final static String inputName = "test_room_name";
    private final static String gameMode = "NORMAL";
    private final static String maxTurn = "20";
    private final static String maxScore = "500";

    @AfterAll
    public static void cleanUp() {
        for (String uid: runningRoomIds) {
            testUtil.deleteRoom(uid);
        }
    }

    //POST valid Room
    @Test
    public void createValidRoom200() throws FileNotFoundException {
        Response response = testUtil.createRoom(inputName, gameMode, maxTurn, maxScore);

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("uid"), is(not(emptyString())));
        assertThat(response.path("name"), is(equalTo(inputName)));
        assertThat(response.path("players"), is(empty()));
        // TODO: add gameSettings asserts

        String roomId = response.path("uid");
        runningRoomIds.add(roomId);
    }

    // POST invalid Room with roomName = ""
    @Test
    public void createRoomEmptyName400() throws IOException {
        Response response = testUtil.createRoom("", gameMode, maxTurn, maxScore);
        assertThat(response.statusCode(), is(equalTo(400)));
    }

    // GET all Rooms
    @Test
    public void getRooms200() {
        Response response = testUtil.getRooms();
        assertThat(response.statusCode(), is(equalTo(200)));
    }

    // GET valid Room
    @Test
    public void getRoom200() throws FileNotFoundException {
        // Room
        String roomId = testUtil.createRoom(inputName, gameMode, maxTurn, maxScore).path("uid");
        runningRoomIds.add(roomId);

        // Request
        Response response = testUtil.getRoom(roomId);
        assertThat(response.statusCode(), is(equalTo(200)));
    }

    // GET invalid Room
    @Test
    public void getRoom404() {
        Response response = testUtil.getRoom("invalid_uid");
        assertThat(response.statusCode(), is(equalTo(404)));
    }

    // GET Players in Room
    @Test
    public void getPlayersInRoom200() throws FileNotFoundException {
        // Room
        String roomId = testUtil.createRoom(inputName, gameMode, maxTurn, maxScore).path("uid");
        runningRoomIds.add(roomId);

        // Request
        Response response = testUtil.getPlayers(roomId);
        assertThat(response.statusCode(), is(equalTo(200)));
    }

    // GET Players in invalid Room
    @Test
    public void getPlayersInRoom404() {
        // Request
        Response response = testUtil.getPlayers("invalid_uid");
        assertThat(response.statusCode(), is(equalTo(404)));
    }

    // DELETE valid Room
    @Test
    public void deleteRoom200() throws FileNotFoundException {
        String roomId = testUtil.createRoom(inputName, gameMode, maxTurn, maxScore).path("uid");
        Response response = testUtil.deleteRoom(roomId);
        assertThat(response.statusCode(), is(equalTo(200)));
    }

    // DELETE invalid Room
    @Test
    public void deleteRoom404() {
        Response response = testUtil.deleteRoom("invalid_uid");
        assertThat(response.statusCode(), is(equalTo(404)));
    }

    // PUT valid name
    @Test
    public void putValidRoom200() throws IOException {
        // Room
        String roomId = testUtil.createRoom(inputName, gameMode, maxTurn, maxScore).path("uid");
        runningRoomIds.add(roomId);

        // Request
        String new_name = "new_name";
        Response response = testUtil.putRoom(new_name, roomId, "Lobby");

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("uid"), is(equalTo(roomId)));
        assertThat(response.path("name"), is(equalTo(new_name)));
    }

    // TODO: add putGameSettings200 - check gameSettings can be updated

    // PUT invalid name
    @Test
    public void putInvalidRoomName400() throws IOException {
        // Room
        String roomId = testUtil.createRoom(inputName, gameMode, maxTurn, maxScore).path("uid");
        runningRoomIds.add(roomId);

        // Request
        Response response = testUtil.putRoom("", roomId, "Lobby");
        assertThat(response.statusCode(), is(equalTo(400)));
    }

    // PUT invalid room status
    @Test
    public void putInvalidRoomStatus400() throws IOException {
        // Room
        String roomId = testUtil.createRoom(inputName, gameMode, maxTurn, maxScore).path("uid");
        runningRoomIds.add(roomId);

        // Request
        Response response = testUtil.putRoom(inputName, roomId, "invalid_status");
        assertThat(response.statusCode(), is(equalTo(400)));
    }

    // PUT invalid room uid
    @Test
    public void putInvalidRoomUid404() throws IOException {
        // Room
        String roomId = testUtil.createRoom(inputName, gameMode, maxTurn, maxScore).path("uid");
        runningRoomIds.add(roomId);

        // Request
        Response response = testUtil.putRoom(inputName, "invalid_uid", "Lobby");
        assertThat(response.statusCode(), is(equalTo(404)));
    }
}
