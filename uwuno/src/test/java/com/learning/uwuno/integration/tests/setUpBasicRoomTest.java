package com.learning.uwuno.integration.tests;

import com.learning.uwuno.integration.jsonUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;

import static com.learning.uwuno.integration.constants.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class setUpBasicRoomTest {
    private String roomId;
    private String playerId;
    private static ArrayList<String> runningRoomIds = new ArrayList<String>();

    @AfterAll
    public static void cleanUp() {
        for (String uid: runningRoomIds) {
            given().pathParam("uid", uid)
                    .when().delete(BASE_URL + "/rooms/{uid}");
        }
    }

    @Test
    @Order(1)
    public void createValidRoom200() throws IOException {
        String filePath = JSON_REQUESTS_PATH + "/postRoom.json";
        String inputName = "room_1";
        String request = jsonUtil.createPostRoomJson(inputName, "false", filePath);

        Response response = given().contentType(ContentType.JSON)
                .when().body(request).post(BASE_URL + "/rooms")
                .then().extract().response();

        System.out.println("Request: " + request);
        System.out.println("Response: " + response.getBody().asString());

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("uid"), is(not(emptyString())));
        assertThat(response.path("name"), is(equalTo(inputName)));
        assertThat(response.path("players"), is(empty()));

        roomId = response.path("uid");
        runningRoomIds.add(roomId);
    }

    @Test
    @Order(2)
    public void createRoomEmptyName400() throws IOException {
        String filePath = JSON_REQUESTS_PATH + "/postRoom.json";
        String request = jsonUtil.createPostRoomJson("", "false", filePath);

        Response response = given().contentType(ContentType.JSON)
                .when().body(request).post(BASE_URL + "/rooms")
                .then().extract().response();

        System.out.println("Request: " + request);
        System.out.println("Response: " + response.getBody().asString());

        assertThat(response.statusCode(), is(equalTo(400)));
    }

    @Test
    @Order(3)
    public void getRooms200() throws IOException {
        Response response = given()
                .when().get(BASE_URL + "/rooms")
                .then().extract().response();

        System.out.println("Response: " + response.getBody().asString());

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.jsonPath().getList("$").size(), is(equalTo(runningRoomIds.size())));
    }

    @Test
    @Order(4)
    public void createValidPlayerInRoom200() throws IOException {
        String filePath = JSON_REQUESTS_PATH + "/postPutPlayer.json";
        String inputName = "player_1";
        String request = jsonUtil.createPostPutPlayerJson(inputName, filePath);

        Response response = given().contentType(ContentType.JSON).pathParam("roomId", roomId)
                .when().body(request).post(BASE_URL + "/rooms/{roomId}/players")
                .then().extract().response();

        System.out.println("Request: " + request);
        System.out.println("Response: " + response.asString());

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("pid"), is(not(emptyString())));
        assertThat(response.path("name"), is(equalTo(inputName)));
        assertThat(response.path("cardList"), is(empty()));

        playerId = response.path("pid");
    }

//    @Test
//    @Order(5)
//    public void changeRoomName200() throws IOException {
//        String filePath = JSON_REQUESTS_PATH + "/putRoom.json";
//        String inputName = "newRoomName";
//        String request = jsonUtil.createPutRoomJson(inputName, roomId, filePath);
//
//        Response response = given().contentType(ContentType.JSON).pathParam("uid", roomId)
//                .when().body(request).put(BASE_URL + "/rooms/{uid}")
//                .then().extract().response();
//
//        System.out.println("Request: " + request);
//        System.out.println("Response: " + response.getBody().asString());
//
//        assertThat(response.statusCode(), is(equalTo(200)));
//        assertThat(response.path("uid"), is(equalTo(roomId)));
//        assertThat(response.path("name"), is(equalTo(inputName)));
//        assertThat(response.jsonPath().getList("findAll {it.players.pid == 'playerId'}"), is());
//
//        roomId = response.path("uid");
//    }
}
