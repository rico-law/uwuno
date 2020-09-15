package com.learning.uwuno.integration.tests;

import com.learning.uwuno.integration.jsonUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.learning.uwuno.integration.constants.BASE_URL;
import static com.learning.uwuno.integration.constants.JSON_REQUESTS_PATH;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class startGameTest {
    private static String roomId;
    private static String roomName;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        // Create room
        String filePath = JSON_REQUESTS_PATH + "/postRoom.json";
        String request = jsonUtil.createPostRoomJson("room", "false", filePath);

        Response response = given().contentType(ContentType.JSON)
                .when().body(request).post(BASE_URL + "/rooms")
                .then().extract().response();

        roomId = response.path("uid");
        roomName = response.path("name");
    }

    @AfterEach
    public void cleanUp() {
        // Delete room
        given().pathParam("uid", roomId)
                .when().delete(BASE_URL + "/rooms/{uid}");
    }

    @Test
    public void putValidStartGame200() throws IOException {
        // Player 1
        String pFilePath = JSON_REQUESTS_PATH + "/postPutPlayer.json";
        String pRequest = jsonUtil.createPostPutPlayerJson("player_1", pFilePath);

        given().contentType(ContentType.JSON).pathParam("uid", roomId)
                .when().body(pRequest).post(BASE_URL + "/rooms/{uid}/players");

        // Player 2
        pRequest = jsonUtil.createPostPutPlayerJson("player_2", pFilePath);
        given().contentType(ContentType.JSON).pathParam("uid", roomId)
                .when().body(pRequest).post(BASE_URL + "/rooms/{uid}/players");

        // Request
        String filePath = JSON_REQUESTS_PATH + "/putRoom.json";
        String status = "Start";
        String request = jsonUtil.createPutRoomJson(roomName, roomId, status, filePath);

        Response response = given().pathParam("uid", roomId)
                .when().body(request).put(BASE_URL + "/rooms/{uid}")
                .then().extract().response();

        Response getRequest = given().pathParam("uid", roomId)
                .when().get(BASE_URL + "/rooms/{uid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(getRequest.path("roomStatus"), is(equalTo(status)));
    }

    @Test
    public void putStartNoPlayers400() throws IOException {
        String filePath = JSON_REQUESTS_PATH + "/putRoom.json";
        String status = "Start";
        String request = jsonUtil.createPutRoomJson(roomName, roomId, status, filePath);

        Response response = given().pathParam("uid", roomId)
                .when().body(request).put(BASE_URL + "/rooms/{uid}")
                .then().extract().response();

        Response getRequest = given().pathParam("uid", roomId)
                .when().get(BASE_URL + "/rooms/{uid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(400)));
        assertThat(getRequest.path("roomStatus"), is("Lobby"));
    }
}
