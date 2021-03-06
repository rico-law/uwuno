package com.learning.uwuno.integration.tests;

import com.learning.uwuno.integration.jsonUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.learning.uwuno.integration.constants.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class basicPlayerTest {
    private static String roomId;
    private final static String inputName = "test_player_name";

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        String filePath = JSON_REQUESTS_PATH + "/postRoom.json";
        String request = jsonUtil.createPostRoomJson("room", "false", filePath);

        roomId = given().contentType(ContentType.JSON)
                .when().body(request).post(BASE_URL + "/rooms")
                .then().extract().response().path("uid");
    }

    @AfterEach
    public void cleanUp() {
        given().pathParam("uid", roomId)
                .when().delete(BASE_URL + "/rooms/{uid}");
    }

    // POST valid Player
    @Test
    public void createValidPlayerInRoom200() throws IOException {
        String filePath = JSON_REQUESTS_PATH + "/postPutPlayer.json";
        String request = jsonUtil.createPostPutPlayerJson(inputName, filePath);

        Response response = given().contentType(ContentType.JSON).pathParam("uid", roomId)
                .when().body(request).post(BASE_URL + "/rooms/{uid}/players")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("pid"), is(not(emptyString())));
        assertThat(response.path("name"), is(equalTo(inputName)));
        assertThat(response.path("cardList"), is(empty()));
    }

    // POST invalid Player with playerName = ""
    @Test
    public void createPlayerEmptyName400() throws IOException {
        String filePath = JSON_REQUESTS_PATH + "/postPutPlayer.json";
        String request = jsonUtil.createPostPutPlayerJson("", filePath);

        Response response = given().contentType(ContentType.JSON).pathParam("uid", roomId)
                .when().body(request).post(BASE_URL + "/rooms/{uid}/players")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(400)));
    }

    // GET Player cards
    @Test
    public void getPlayerCards200() throws IOException {
        // Player
        String playerFilePath = JSON_REQUESTS_PATH + "/postPutPlayer.json";
        String playerRequest = jsonUtil.createPostPutPlayerJson(inputName, playerFilePath);

        String playerId = given().contentType(ContentType.JSON).pathParam("uid", roomId)
                .when().body(playerRequest).post(BASE_URL + "/rooms/{uid}/players")
                .then().extract().response().path("pid");

        // Request
        Response response = given().pathParam("uid", roomId).pathParam("pid", playerId)
                .when().get(BASE_URL + "/rooms/{uid}/players/{pid}/cards")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(200)));
    }

    // GET invalid Player cards - via invalid pid
    @Test
    public void getPlayerCardsInvalidPid404() {
        Response response = given().pathParam("uid", roomId).pathParam("pid", "invalid_pid")
                .when().get(BASE_URL + "/rooms/{uid}/players/{pid}/cards")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(404)));
    }

    // GET invalid Player cards - via invalid uid
    @Test
    public void getPlayerCardsInvalidUid404() throws IOException {
        // Player
        String playerFilePath = JSON_REQUESTS_PATH + "/postPutPlayer.json";
        String playerRequest = jsonUtil.createPostPutPlayerJson(inputName, playerFilePath);

        String playerId = given().contentType(ContentType.JSON).pathParam("uid", roomId)
                .when().body(playerRequest).post(BASE_URL + "/rooms/{uid}/players")
                .then().extract().response().path("pid");

        // Request
        Response response = given().pathParam("uid", "invalid_uid").pathParam("pid", playerId)
                .when().get(BASE_URL + "/rooms/{uid}/players/{pid}/cards")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(404)));
    }

    // DELETE valid Player
    @Test
    public void deletePlayer200() throws IOException {
        // Player
        String playerFilePath = JSON_REQUESTS_PATH + "/postPutPlayer.json";
        String playerRequest = jsonUtil.createPostPutPlayerJson(inputName, playerFilePath);

        String playerId = given().contentType(ContentType.JSON).pathParam("uid", roomId)
                .when().body(playerRequest).post(BASE_URL + "/rooms/{uid}/players")
                .then().extract().response().path("pid");

        // Request
        Response response = given().pathParam("uid", roomId).pathParam("pid", playerId)
                .when().delete(BASE_URL + "/rooms/{uid}/players/{pid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(200)));
    }

    // DELETE invalid Player - via invalid pid
    @Test
    public void deletePlayerInvalidPid404() {
        Response response = given().pathParam("uid", roomId).pathParam("pid", "invalid_pid")
                .when().delete(BASE_URL + "/rooms/{uid}/players/{pid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(404)));
    }

    // DELETE invalid Player - via invalid uid
    @Test
    public void deletePlayerInvalidUid404() throws FileNotFoundException {
        // Player
        String playerFilePath = JSON_REQUESTS_PATH + "/postPutPlayer.json";
        String playerRequest = jsonUtil.createPostPutPlayerJson(inputName, playerFilePath);

        String playerId = given().contentType(ContentType.JSON).pathParam("uid", roomId)
                .when().body(playerRequest).post(BASE_URL + "/rooms/{uid}/players")
                .then().extract().response().path("pid");

        // Request
        Response response = given().pathParam("uid", "invalid_uid").pathParam("pid", playerId)
                .when().delete(BASE_URL + "/rooms/{uid}/players/{pid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(404)));
    }

    // PUT valid name
    @Test
    public void putValidPlayerName200() throws FileNotFoundException {
        // Player
        String playerFilePath = JSON_REQUESTS_PATH + "/postPutPlayer.json";
        String playerInputName = "player_put_old_name_valid";
        String playerRequest = jsonUtil.createPostPutPlayerJson(playerInputName, playerFilePath);

        String playerId = given().contentType(ContentType.JSON).pathParam("uid", roomId)
                .when().body(playerRequest).post(BASE_URL + "/rooms/{uid}/players")
                .then().extract().response().path("pid");

        // Request
        String filePath = JSON_REQUESTS_PATH + "/postPutPlayer.json";
        String request = jsonUtil.createPostPutPlayerJson(inputName, filePath);

        Response response = given().pathParam("uid", roomId).pathParam("pid", playerId)
                .when().body(request).put(BASE_URL + "/rooms/{uid}/players/{pid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("pid"), is(equalTo(playerId)));
        assertThat(response.path("name"), is(equalTo(inputName)));
    }

    // PUT invalid name
    @Test
    public void putInvalidPlayerName400() throws FileNotFoundException {
        // Player
        String playerFilePath = JSON_REQUESTS_PATH + "/postPutPlayer.json";
        String playerRequest = jsonUtil.createPostPutPlayerJson(inputName, playerFilePath);

        String playerId = given().contentType(ContentType.JSON).pathParam("uid", roomId)
                .when().body(playerRequest).post(BASE_URL + "/rooms/{uid}/players")
                .then().extract().response().path("pid");

        // Request
        String filePath = JSON_REQUESTS_PATH + "/postPutPlayer.json";
        String request = jsonUtil.createPostPutPlayerJson("", filePath);

        Response response = given().pathParam("uid", roomId).pathParam("pid", playerId)
                .when().body(request).put(BASE_URL + "/rooms/{uid}/players/{pid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(400)));
    }

    // PUT invalid name
    @Test
    public void putInvalidPlayerName404() throws FileNotFoundException {
        // Player
        String playerFilePath = JSON_REQUESTS_PATH + "/postPutPlayer.json";
        String playerRequest = jsonUtil.createPostPutPlayerJson(inputName, playerFilePath);

        String playerId = given().contentType(ContentType.JSON).pathParam("uid", roomId)
                .when().body(playerRequest).post(BASE_URL + "/rooms/{uid}/players")
                .then().extract().response().path("pid");

        // Request
        String filePath = JSON_REQUESTS_PATH + "/postPutPlayer.json";
        String request = jsonUtil.createPostPutPlayerJson("", filePath);

        Response response = given().pathParam("uid", roomId).pathParam("pid", playerId)
                .when().body(request).put(BASE_URL + "/rooms/{uid}/players/{pid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(400)));
    }

    // PUT draw card
//    @Test
//    public void putPlayerDrawCards() throws FileNotFoundException {
//        // Player
//        String playerFilePath = JSON_REQUESTS_PATH + "/postPutPlayer.json";
//        String playerInputName = "put_player_draw_cards_200";;
//        String playerRequest = jsonUtil.createPostPutPlayerJson(playerInputName, playerFilePath);
//
//        String playerId = given().contentType(ContentType.JSON).pathParam("uid", roomId)
//                .when().body(playerRequest).post(BASE_URL + "/rooms/{uid}/players")
//                .then().extract().response().path("pid");
//
//        // Request
//        String filePath = JSON_REQUESTS_PATH + "/putPlayerDrawCard.json";
//        String inputNum = "5";
//        String request = jsonUtil.createPutPlayerDrawCardJson(inputNum, filePath);
//
//        Response response = given().pathParam("uid", roomId).pathParam("pid", playerId)
//                .when().body(request).put(BASE_URL + "/rooms/{uid}/players/{pid}")
//                .then().extract().response();
//
//        assertThat(response.statusCode(), is(equalTo(200)));
////        assertThat(response.path())
//    }
}
