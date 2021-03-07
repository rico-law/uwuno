package com.learning.uwuno.integration.tests;

import com.learning.uwuno.integration.testUtil;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.FileNotFoundException;

public class skipTurnTest {
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
        roomId = testUtil.createRoom(roomName, gameMode, "20", "500").path("uid");
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

    // TODO: need to check whether response JSON have actual valid values
}
