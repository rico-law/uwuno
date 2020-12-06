package com.learning.uwuno.integration.tests;

import com.learning.uwuno.integration.jsonUtil;
import static com.learning.uwuno.integration.constants.BASE_URL;
import static com.learning.uwuno.integration.constants.JSON_REQUESTS_PATH;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class gameSettingsTest {
    private static String roomId;
    private static final String testStatus = "Lobby";
    private static String putPointRoomJSON;
    private static String putNormalRoomJSON;
    private static final int maxTurn = 30;
    private static final int pointMinScore = 500;
    private static final int normalDefaultScore = 0;
    private static final boolean useBlank = false;
    private static final String pointMode = "POINT";
    private static final String normalMode = "NORMAL";


    @BeforeEach
    public void setUp() throws FileNotFoundException {
        // Create room
        String filePath = JSON_REQUESTS_PATH + "/postRoom.json";
        String request = jsonUtil.createPostRoomJson("room", "false", filePath);

        Response response = given().contentType(ContentType.JSON)
                .when().body(request).post(BASE_URL + "/rooms")
                .then().extract().response();

        roomId = response.path("uid");

        // Setup JSON PUT request from template
        putPointRoomJSON = jsonUtil.createPutGameSettings(roomId, pointMode, String.valueOf(maxTurn),String.valueOf(pointMinScore),
                String.valueOf(useBlank), JSON_REQUESTS_PATH + "/putRoomGameSettings.json");
        putNormalRoomJSON = jsonUtil.createPutGameSettings(roomId, normalMode, String.valueOf(maxTurn),String.valueOf(100),
                String.valueOf(useBlank), JSON_REQUESTS_PATH + "/putRoomGameSettings.json");
    }

    @AfterEach
    public void cleanUp() {
        // Delete room
        given().pathParam("uid", roomId)
                .when().delete(BASE_URL + "/rooms/{uid}");
    }

    // PUT game setting - point mode
    @Test
    public void putValidPointSetting200() {
        Response response = given().pathParam("uid", roomId)
                .when().body(putPointRoomJSON).put(BASE_URL + "/rooms/{uid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("gameSettings.maxTurn"), is(equalTo(maxTurn)));
        assertThat(response.path("gameSettings.maxScore"), is(equalTo(pointMinScore)));
        assertThat(response.path("gameSettings.useBlankCards"), is(equalTo(useBlank)));
        assertThat(response.path("gameSettings.gameMode.modeName"), is(equalTo(pointMode)));
    }

    // PUT game setting - normal mode (maxScore should always be 0, regardless of input maxScore)
    @Test
    public void putValidNormalSetting200() {
        Response response = given().pathParam("uid", roomId)
                .when().body(putNormalRoomJSON).put(BASE_URL + "/rooms/{uid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("gameSettings.maxTurn"), is(equalTo(maxTurn)));
        assertThat(response.path("gameSettings.maxScore"), is(equalTo(normalDefaultScore)));
        assertThat(response.path("gameSettings.useBlankCards"), is(equalTo(useBlank)));
        assertThat(response.path("gameSettings.gameMode.modeName"), is(equalTo(normalMode)));
    }
}
