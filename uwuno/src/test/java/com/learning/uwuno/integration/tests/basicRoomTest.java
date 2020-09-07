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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class basicRoomTest {
    private static ArrayList<String> runningRoomIds = new ArrayList<String>();

    @AfterAll
    public static void cleanUp() {
        for (String uid: runningRoomIds) {
            given().pathParam("uid", uid)
                    .when().delete(BASE_URL + "/rooms/{uid}");
        }
    }

    //POST valid Room
    @Test
    public void createValidRoom200() throws IOException {
        String filePath = JSON_REQUESTS_PATH + "/postRoom.json";
        String inputName = "room_post_valid_200";
        String request = jsonUtil.createPostRoomJson(inputName, "false", filePath);

        Response response = given().contentType(ContentType.JSON)
                .when().body(request).post(BASE_URL + "/rooms")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("uid"), is(not(emptyString())));
        assertThat(response.path("name"), is(equalTo(inputName)));
        assertThat(response.path("players"), is(empty()));

        String roomId = response.path("uid");
        runningRoomIds.add(roomId);
    }

    // POST invalid Room with roomName = ""
    @Test
    public void createRoomEmptyName400() throws IOException {
        String filePath = JSON_REQUESTS_PATH + "/postRoom.json";
        String request = jsonUtil.createPostRoomJson("", "false", filePath);

        Response response = given().contentType(ContentType.JSON)
                .when().body(request).post(BASE_URL + "/rooms")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(400)));
    }

    // GET all Rooms
    @Test
    public void getRooms200() {
        Response response = given()
                .when().get(BASE_URL + "/rooms")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(200)));
    }

    // GET valid Room
    @Test
    public void getRoom200() throws IOException {
        // Room
        String filePath = JSON_REQUESTS_PATH + "/postRoom.json";
        String inputName = "room_get_room_200";
        String request = jsonUtil.createPostRoomJson(inputName, "false", filePath);

        String roomId = given().contentType(ContentType.JSON)
                .when().body(request).post(BASE_URL + "/rooms")
                .then().extract().response().path("uid");

        runningRoomIds.add(roomId);

        // Request
        Response response = given().pathParam("uid", roomId)
                .when().get(BASE_URL + "/rooms/{uid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(200)));
    }

    // GET invalid Room
    @Test
    public void getRoom404() {
        Response response = given().pathParam("uid", "invalid_uid")
                .when().get(BASE_URL + "/rooms/{uid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(404)));
    }

    // GET Players in Room
    @Test
    public void getPlayersInRoom200() throws IOException {
        // Room
        String filePath = JSON_REQUESTS_PATH + "/postRoom.json";
        String inputName = "room_get_players_200";
        String request = jsonUtil.createPostRoomJson(inputName, "false", filePath);

        String roomId = given().contentType(ContentType.JSON)
                .when().body(request).post(BASE_URL + "/rooms")
                .then().extract().response().path("uid");

        runningRoomIds.add(roomId);

        // Request
        Response response = given().pathParam("uid", roomId)
                .when().get(BASE_URL + "/rooms/{uid}/players")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(200)));
    }

    // GET Players in invalid Room
    @Test
    public void getPlayersInRoom404() throws IOException {
        // Request
        Response response = given().pathParam("uid", "invalid_uid")
                .when().get(BASE_URL + "/rooms/{uid}/players")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(404)));
    }

    // DELETE valid Room
    @Test
    public void deleteRoom200() throws IOException {
        // Room
        String filePath = JSON_REQUESTS_PATH + "/postRoom.json";
        String inputName = "room_delete_valid_200";
        String request = jsonUtil.createPostRoomJson(inputName, "false", filePath);

        String roomId = given().contentType(ContentType.JSON)
                .when().body(request).post(BASE_URL + "/rooms")
                .then().extract().response().path("uid");

        // Request
        Response response = given().pathParam("uid", roomId)
                .when().delete(BASE_URL + "/rooms/{uid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(200)));
    }

    // DELETE invalid Room
    @Test
    public void deleteRoom404() throws IOException {
        Response response = given().pathParam("uid", "invalid_uid")
                .when().delete(BASE_URL + "/rooms/{uid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(404)));
    }

    // PUT valid name
    @Test
    public void putValidRoomName200() throws IOException {
        // Room
        String oldFilePath = JSON_REQUESTS_PATH + "/postRoom.json";
        String oldInputName = "room_put_old_name_valid";
        String oldRequest = jsonUtil.createPostRoomJson(oldInputName, "false", oldFilePath);

        String roomId = given().contentType(ContentType.JSON)
                .when().body(oldRequest).post(BASE_URL + "/rooms")
                .then().extract().response().path("uid");

        runningRoomIds.add(roomId);

        // Request
        String filePath = JSON_REQUESTS_PATH + "/putRoom.json";
        String inputName = "room_put_new_name_valid_200";
        String request = jsonUtil.createPutRoomJson(inputName, roomId, filePath);

        Response response = given().pathParam("uid", roomId)
                .when().body(request).put(BASE_URL + "/rooms/{uid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("name"), is(equalTo(inputName)));
    }

    // PUT invalid name
    @Test
    public void putValidRoomName400() throws IOException {
        // Room
        String oldFilePath = JSON_REQUESTS_PATH + "/postRoom.json";
        String oldInputName = "room_put_old_name_invalid_400";
        String oldRequest = jsonUtil.createPostRoomJson(oldInputName, "false", oldFilePath);

        String roomId = given().contentType(ContentType.JSON)
                .when().body(oldRequest).post(BASE_URL + "/rooms")
                .then().extract().response().path("uid");

        runningRoomIds.add(roomId);

        // Request
        String filePath = JSON_REQUESTS_PATH + "/putRoom.json";
        String request = jsonUtil.createPutRoomJson("", roomId, filePath);

        Response response = given().pathParam("uid", roomId)
                .when().body(request).put(BASE_URL + "/rooms/{uid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(400)));
    }

    // PUT invalid room uid
    @Test
    public void putValidRoomName404() throws IOException {
        String filePath = JSON_REQUESTS_PATH + "/putRoom.json";
        String inputName = "room_put_new_name_valid_200";
        String request = jsonUtil.createPutRoomJson(inputName, "invalid_uid", filePath);

        Response response = given().pathParam("uid", "invalid_uid")
                .when().body(request).put(BASE_URL + "/rooms/{uid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(404)));
    }
}