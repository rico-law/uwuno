package com.learning.uwuno.integration.tests;

import com.learning.uwuno.integration.testUtil;

import java.io.FileNotFoundException;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class playCardTest {
    private static String roomId;
    private static String playerId1;
    private static String playerId2;
    private static String playerTurnId;
    private static final String roomName = "room";
    private static final String testStatus = "Start";
    private static final String gameMode = "NORMAL";

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        // Start game
        roomId = testUtil.createRoom(roomName).path("uid");
        playerId1 = testUtil.createPlayer("player_1", roomId).path("pid");
        playerId2 = testUtil.createPlayer("player_2", roomId).path("pid");
        testUtil.putGameSettings(roomId, gameMode, "30", "0", "false");
        Response response = testUtil.putRoom(roomName, roomId, testStatus);
        playerTurnId = response.path("playerTurn.pid");
    }

    @AfterEach
    public void cleanUp() {
        testUtil.deleteRoom(roomId);
    }

    // PUT player skips turn
    @Test
    public void putSkipTurn200() throws FileNotFoundException {
        Response response = testUtil.playerPlayCard(roomId, playerTurnId, "", "", "",
                "", "true");
        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("playerTurnPid"), is(not(emptyString())));
        assertThat(response.path("playableCards"), is(not(empty())));
    }

    // PUT player invalid skip turn - via having card info while skipping
    @Test
    public void putInvalidSkipTurn400() throws FileNotFoundException {
        Response response = testUtil.playerPlayCard(roomId, playerTurnId, "Basic", "Yellow", "2",
                "", "true");
        assertThat(response.statusCode(), is(equalTo(400)));
    }

    // PUT player invalid request - via incorrect player's turn
    @Test
    public void putInvalidPlayerTurn400() throws FileNotFoundException {
        String playerId = (playerTurnId.equals(playerId1)) ? playerId2 : playerId1;
        Response response = testUtil.playerPlayCard(roomId, playerId, "", "", "",
                "", "true");
        assertThat(response.statusCode(), is(equalTo(400)));
    }
}