package com.learning.uwuno.integration.tests;

import com.learning.uwuno.integration.jsonUtil;
import com.learning.uwuno.integration.testUtil;
import static com.learning.uwuno.integration.constants.BASE_URL;
import static com.learning.uwuno.integration.constants.JSON_REQUESTS_PATH;

import static io.restassured.RestAssured.given;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class playCardTest {
    private static String roomId;
    private static String roomName = "room_1";
    private static final String testStatus = "Start";

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        // Create room
        roomId = testUtil.createRoom(roomName).path("uid");

        // Add players
        testUtil.createPlayer("player_1", roomId);
        testUtil.createPlayer("player_2", roomId);

        // Start game
        testUtil.putRoom(roomName, roomId, testStatus);
    }

    @AfterEach
    public void cleanUp() {
        // Delete room
        given().pathParam("uid", roomId)
                .when().delete(BASE_URL + "/rooms/{uid}");
    }
}