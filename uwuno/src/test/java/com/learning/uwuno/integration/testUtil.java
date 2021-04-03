package com.learning.uwuno.integration;

import static com.learning.uwuno.integration.constants.BASE_URL;
import static com.learning.uwuno.integration.constants.JSON_REQUESTS_PATH;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import java.io.FileNotFoundException;

// All params should be String (simulate JSON requests)
public final class testUtil {
    private static final String postRoom = JSON_REQUESTS_PATH + "/postRoom.json";
    private static final String putRoom = JSON_REQUESTS_PATH + "/putRoom.json";
    private static final String putGameSettings = JSON_REQUESTS_PATH + "/putRoomGameSettings.json";
    private static final String postPutPlayer = JSON_REQUESTS_PATH + "/postPutPlayer.json";
    private static final String putPlayerDrawCard = JSON_REQUESTS_PATH + "/putPlayerDrawCard.json";
    private static final String putPlayerPlayCard = JSON_REQUESTS_PATH + "/putPlayerPlayCard.json";

    private testUtil() {
        // Should not run
    }

    // POST - Create room - returns room response
    public static Response createRoom(String roomName, String gameMode, String maxTurn, String maxScore)
            throws FileNotFoundException {
        String request = jsonUtil.createPostRoomJson(roomName, "false", gameMode, maxTurn, maxScore, postRoom);
        return given().contentType(ContentType.JSON)
                .when().body(request).post(BASE_URL + "/rooms")
                .then().extract().response();
    }

    // POST - Create player - returns player response
    public static Response createPlayer(String playerName, String roomId) throws FileNotFoundException {
        String request = jsonUtil.createPostPutPlayerJson(playerName, postPutPlayer);
        return given().contentType(ContentType.JSON).pathParam("uid", roomId)
                .when().body(request).post(BASE_URL + "/rooms/{uid}/players")
                .then().extract().response();
    }

    // PUT - Change room name or status - returns room response
    public static Response putRoom(String roomName, String roomId, String targetStatus) throws FileNotFoundException {
        String request = jsonUtil.createPutRoomJson(roomName, roomId, targetStatus, putRoom);
        return given().pathParam("uid", roomId)
                .when().body(request).put(BASE_URL + "/rooms/{uid}")
                .then().extract().response();
    }

    // PUT - Set game settings - returns room response
    // TODO: remove roomID from JSON, already present in endpoint (redundant)
    public static Response putGameSettings(String roomId, String gameMode, String maxTurn, String maxScore, String useBlank)
            throws FileNotFoundException {
        String request = jsonUtil.createPutGameSettings(roomId, gameMode, maxTurn, maxScore,useBlank, putGameSettings);
        return given().pathParam("uid", roomId)
                .when().body(request).put(BASE_URL + "/rooms/{uid}/settings")
                .then().extract().response();
    }

    // PUT - Change player name - returns player response
    public static Response changePlayerName(String roomId, String playerId, String playerName) throws FileNotFoundException {
        String request = jsonUtil.createPostPutPlayerJson(playerName, postPutPlayer);
        return given().pathParam("uid", roomId).pathParam("pid", playerId)
                .when().body(request).put(BASE_URL + "/rooms/{uid}/players/{pid}")
                .then().extract().response();
    }

    // PUT - Player draw cards - returns player response
    public static Response playerDrawCards(String drawNumCards, String roomId, String playerId) throws FileNotFoundException {
        String request = jsonUtil.createPutPlayerDrawCardJson(drawNumCards, putPlayerDrawCard);
        return given().pathParam("uid", roomId).pathParam("pid", playerId)
                .when().body(request).put(BASE_URL + "/rooms/{uid}/players/{pid}")
                .then().extract().response();
    }

    // PUT - Player play card - returns gameResponse response
    public static Response playerPlayCard(String roomId, String playerId, String cardType, String cardColor,
                                          String cardValue, String setWildColor, String skip) throws FileNotFoundException {
        String request = jsonUtil.createPutPlayerPlayCardJson(cardType, cardColor, cardValue, setWildColor, skip, putPlayerPlayCard);
        return given().pathParam("uid", roomId).pathParam("pid", playerId)
                .when().body(request).put(BASE_URL + "/rooms/{uid}/players/{pid}")
                .then().extract().response();
    }

    // GET - Get room - returns room response
    public static Response getRoom(String roomId) {
        return given().pathParam("uid", roomId)
                .when().get(BASE_URL + "/rooms/{uid}")
                .then().extract().response();
    }

    // GET - Get all rooms - return response for list of rooms
    public static Response getRooms() {
        return given()
                .when().get(BASE_URL + "/rooms")
                .then().extract().response();
    }

    // GET - Get all players in given room - return response for list of players
    public static Response getPlayers(String roomId) {
        return given().pathParam("uid", roomId)
                .when().get(BASE_URL + "/rooms/{uid}/players")
                .then().extract().response();
    }

    // GET - Get player hand cards - returns player's list of cards response
    public static Response getPlayerCards(String roomId, String playerId) {
        return given().pathParam("uid", roomId).pathParam("pid", playerId)
                .when().get(BASE_URL + "/rooms/{uid}/players/{pid}/cards")
                .then().extract().response();
    }

    // DELETE - Delete room - returns void response
    public static Response deleteRoom(String roomId) {
        return given().pathParam("uid", roomId)
                .when().delete(BASE_URL + "/rooms/{uid}");
    }

    // DELETE - Delete player - returns void response
    public static Response deletePlayer(String roomId, String playerId) {
        return given().pathParam("uid", roomId).pathParam("pid", playerId)
                .when().delete(BASE_URL + "/rooms/{uid}/players/{pid}")
                .then().extract().response();
    }
}
