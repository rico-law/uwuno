package com.learning.uwuno.integration.tests;

import com.learning.uwuno.integration.testUtil;

import io.restassured.response.Response;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class gameSettingsTest {
    private static String roomId;
    private static final String testStatus = "Start";
    private static final int maxTurn = 15;
    private static final int pointMinScore = 500;
    private static final int normalDefaultScore = 0;
    private static final boolean useBlank = false;
    private static final String pointMode = "POINT";
    private static final String normalMode = "NORMAL";

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        roomId = testUtil.createRoom("room_1", normalMode, Integer.toString(maxTurn), "500").path("uid");
    }

    @AfterEach
    public void cleanUp() {
        testUtil.deleteRoom(roomId);
    }

    // PUT game setting - point mode
    @Test
    public void putValidPointSetting200() throws FileNotFoundException {
        Response response = testUtil.putGameSettings(roomId, pointMode, String.valueOf(maxTurn),
                String.valueOf(pointMinScore), String.valueOf(useBlank));

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("gameSettings.maxTurn"), is(equalTo(maxTurn)));
        assertThat(response.path("gameSettings.maxScore"), is(equalTo(pointMinScore)));
        assertThat(response.path("gameSettings.useBlankCards"), is(equalTo(useBlank)));
        assertThat(response.path("gameSettings.gameMode.modeName"), is(equalTo(pointMode)));
    }

    // PUT game setting - normal mode (maxScore should always be 0, regardless of input maxScore)
    @Test
    public void putValidNormalSetting200() throws FileNotFoundException {
        Response response = testUtil.putGameSettings(roomId, normalMode, String.valueOf(maxTurn), "100",
                String.valueOf(useBlank));

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("gameSettings.maxTurn"), is(equalTo(maxTurn)));
        assertThat(response.path("gameSettings.maxScore"), is(equalTo(normalDefaultScore)));
        assertThat(response.path("gameSettings.useBlankCards"), is(equalTo(useBlank)));
        assertThat(response.path("gameSettings.gameMode.modeName"), is(equalTo(normalMode)));
    }

    // PUT - invalid setting via maxTurn < 15
    @Test
    public void putInvalidMaxTurn400() throws FileNotFoundException {
        Response response = testUtil.putGameSettings(roomId, normalMode, "10", String.valueOf(pointMinScore),
                String.valueOf(useBlank));
        assertThat(response.statusCode(), is(equalTo(400)));
    }

    // PUT - invalid setting via maxScore < 500 (point mode)
    @Test
    public void putInvalidMaxScore400() throws FileNotFoundException {
        Response response = testUtil.putGameSettings(roomId, pointMode, String.valueOf(maxTurn), "100",
                String.valueOf(useBlank));
        assertThat(response.statusCode(), is(equalTo(400)));
    }

    // PUT - invalid setting via room status not Lobby
    @Test
    public void putInvalidRoomStatus400() throws FileNotFoundException {
        // Add 2 players and change room status to Start
        testUtil.createPlayer("player_1", roomId);
        testUtil.createPlayer("player_2", roomId);
        testUtil.putRoom("room", roomId, testStatus);

        // Request
        Response response = testUtil.putGameSettings(roomId, pointMode, String.valueOf(maxTurn),
                String.valueOf(pointMinScore), String.valueOf(useBlank));
        assertThat(response.statusCode(), is(equalTo(400)));
    }
}
