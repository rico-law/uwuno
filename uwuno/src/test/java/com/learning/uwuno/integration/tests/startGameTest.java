package com.learning.uwuno.integration.tests;

import com.learning.uwuno.integration.jsonUtil;
import static com.learning.uwuno.integration.testUtil.createPlayer;
import static com.learning.uwuno.integration.constants.BASE_URL;
import static com.learning.uwuno.integration.constants.JSON_REQUESTS_PATH;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class startGameTest {
    private static String roomId;
    private static String putRoomJSON;
    private static final String testStatus = "Start";
    private static String putDrawCardJSON;
    private static String invalidPutDrawJSON;
    private static final int startingHandCardSize = 7;
    private static final int totalCards = 108;
    private static final int drawNumCards = 5;

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
        putRoomJSON = jsonUtil.createPutRoomJson(response.path("name"), roomId, testStatus, JSON_REQUESTS_PATH + "/putRoom.json");
        putDrawCardJSON = jsonUtil.createPutPlayerDrawCardJson(Integer.toString(drawNumCards), JSON_REQUESTS_PATH + "/putPlayerDrawCard.json");
        invalidPutDrawJSON = jsonUtil.createPutPlayerDrawCardJson("invalid", JSON_REQUESTS_PATH + "/putPlayerDrawCard.json");
    }

    @AfterEach
    public void cleanUp() {
        // Delete room
        given().pathParam("uid", roomId)
                .when().delete(BASE_URL + "/rooms/{uid}");
    }

    // PUT request to Start game
    @Test
    public void putValidStartGame200() throws FileNotFoundException {
        createPlayer("player_1", roomId);
        createPlayer("player_2", roomId);
        Response response = given().pathParam("uid", roomId)
                .when().body(putRoomJSON).put(BASE_URL + "/rooms/{uid}")
                .then().extract().response();

        List<Object> cardList1 = response.jsonPath().getList("players[0].cardList");
        List<Object> cardList2 = response.jsonPath().getList("players[1].cardList");

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("roomStatus"), is(equalTo(testStatus)));
        assertThat(response.path("activeDeckSize"), is(equalTo(totalCards - startingHandCardSize*2 - 1)));
        assertThat(response.path("lastPlayedCard"), is(not(equalTo(null))));
        assertThat(response.jsonPath().getList("discardPile").size(), is(equalTo(0)));
        assertThat(cardList1.size(), is(equalTo(startingHandCardSize)));
        assertThat(cardList2.size(), is(equalTo(startingHandCardSize)));
    }

    // PUT invalid Start game request - via no players
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

    // PUT draw card
    @Test
    public void putPlayerDrawCards200() throws FileNotFoundException {
        Response player1 = createPlayer("player_1", roomId);
        createPlayer("player_2", roomId);

        // Change room status to Start
        given().pathParam("uid", roomId)
                .when().body(putRoomJSON).put(BASE_URL + "/rooms/{uid}");

        // Request
        Response response = given().pathParam("uid", roomId).pathParam("pid", player1.path("pid"))
                .when().body(putDrawCardJSON).put(BASE_URL + "/rooms/{uid}/players/{pid}")
                .then().extract().response();

        Response getResponse = given().pathParam("uid", roomId)
                .when().get(BASE_URL + "/rooms/{uid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.jsonPath().getList("cardList").size(), is(equalTo(startingHandCardSize + drawNumCards)));
        assertThat(getResponse.path("activeDeckSize"), is(equalTo(totalCards - startingHandCardSize*2 - drawNumCards - 1)));
    }

    // PUT invalid draw card - via non-numeric number
    @Test
    public void putInvalidDrawCards400() throws FileNotFoundException {
        Response player1 = createPlayer("player_1", roomId);
        createPlayer("player_2", roomId);

        // Change room status to Start
        given().pathParam("uid", roomId)
                .when().body(putRoomJSON).put(BASE_URL + "/rooms/{uid}");

        // Request
        Response response = given().pathParam("uid", roomId).pathParam("pid", player1.path("pid"))
                .when().body(invalidPutDrawJSON).put(BASE_URL + "/rooms/{uid}/players/{pid}")
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(400)));
    }
}
