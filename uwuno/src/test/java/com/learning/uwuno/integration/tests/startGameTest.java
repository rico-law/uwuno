package com.learning.uwuno.integration.tests;

import com.learning.uwuno.integration.jsonUtil;
import static com.learning.uwuno.integration.constants.BASE_URL;
import static com.learning.uwuno.integration.constants.JSON_REQUESTS_PATH;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class startGameTest {
    private static String roomId;
    private static String putRoomJSON;
    private static final String testStatus = "Start";
    private static final String postPutPlayerPath = JSON_REQUESTS_PATH + "/postPutPlayer.json";

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        // Create room
        String filePath = JSON_REQUESTS_PATH + "/postRoom.json";
        String request = jsonUtil.createPostRoomJson("room", "false", filePath);

        Response response = given().contentType(ContentType.JSON)
                .when().body(request).post(BASE_URL + "/rooms")
                .then().extract().response();

        roomId = response.path("uid");

        // Setup JSON requests from templates
        putRoomJSON = jsonUtil.createPutRoomJson(response.path("name"), roomId,
                testStatus, JSON_REQUESTS_PATH + "/putRoom.json");
    }

    @AfterEach
    public void cleanUp() {
        // Delete room
        given().pathParam("uid", roomId)
                .when().delete(BASE_URL + "/rooms/{uid}");
    }

    @Test
    public void putValidStartGame200() throws IOException {
        // Create 2 players
        for (int i = 0; i < 2; i++) {
            String request = jsonUtil.createPostPutPlayerJson("player_" + i, postPutPlayerPath);
            given().contentType(ContentType.JSON).pathParam("uid", roomId)
                    .when().body(request).post(BASE_URL + "/rooms/{uid}/players");
        }

        // PUT request to Start game
        Response response = given().pathParam("uid", roomId)
                .when().body(putRoomJSON).put(BASE_URL + "/rooms/{uid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("roomStatus"), is(equalTo(testStatus)));
    }

    @Test
    public void putStartNoPlayers400() {
        Response response = given().pathParam("uid", roomId)
                .when().body(putRoomJSON).put(BASE_URL + "/rooms/{uid}")
                .then().extract().response();

        Response getRequest = given().pathParam("uid", roomId)
                .when().get(BASE_URL + "/rooms/{uid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(400)));
        assertThat(getRequest.path("roomStatus"), is("Lobby"));
    }
}
