//package com.learning.uwuno.integration.tests;
//
//import com.learning.uwuno.integration.jsonUtil;
//import io.restassured.http.ContentType;
//import io.restassured.response.Response;
//import org.junit.jupiter.api.*;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//import static com.learning.uwuno.integration.constants.*;
//import static io.restassured.RestAssured.*;
//import static org.hamcrest.MatcherAssert.*;
//import static org.hamcrest.Matchers.*;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class basicPlayerTest {
//    private static ArrayList<String> runningRoomIds = new ArrayList<String>();
//
//    @AfterAll
//    public static void cleanUp() {
//        for (String uid: runningRoomIds) {
//            given().pathParam("uid", uid)
//                    .when().delete(BASE_URL + "/rooms/{uid}");
//        }
//    }
//
//    // POST valid Player
//    @Test
//    public void createValidPlayerInRoom200() throws IOException {
//        // Room
//        String roomFilePath = JSON_REQUESTS_PATH + "/postRoom.json";
//        String roomInputName = "room_with_valid_player";
//        String roomRequest = jsonUtil.createPostRoomJson(roomInputName, "false", roomFilePath);
//
//        String roomId = given().contentType(ContentType.JSON)
//                .when().body(roomRequest).post(BASE_URL + "/rooms")
//                .then().extract().response().path("uid");
//
//        runningRoomIds.add(roomId);
//
//        // Request
//        String filePath = JSON_REQUESTS_PATH + "/postPutPlayer.json";
//        String inputName = "player_valid_200";
//        String request = jsonUtil.createPostPutPlayerJson(inputName, filePath);
//
//        Response response = given().contentType(ContentType.JSON).pathParam("roomId", roomId)
//                .when().body(request).post(BASE_URL + "/rooms/{roomId}/players")
//                .then().extract().response();
//
//        assertThat(response.statusCode(), is(equalTo(200)));
//        assertThat(response.path("pid"), is(not(emptyString())));
//        assertThat(response.path("name"), is(equalTo(inputName)));
//        assertThat(response.path("cardList"), is(empty()));
//    }
//
//    // POST invalid Player with playerName = ""
//    @Test
//    public void createPlayerEmptyName400() throws IOException {
//        // Room
//        String roomFilePath = JSON_REQUESTS_PATH + "/postRoom.json";
//        String roomInputName = "room_with_invalid_player";
//        String roomRequest = jsonUtil.createPostRoomJson(roomInputName, "false", roomFilePath);
//
//        String roomId = given().contentType(ContentType.JSON)
//                .when().body(roomRequest).post(BASE_URL + "/rooms")
//                .then().extract().response().path("uid");
//
//        runningRoomIds.add(roomId);
//
//        // Request
//        String filePath = JSON_REQUESTS_PATH + "/postPutPlayer.json";
//        String inputName = "";
//        String request = jsonUtil.createPostPutPlayerJson(inputName, filePath);
//
//        Response response = given().contentType(ContentType.JSON).pathParam("roomId", roomId)
//                .when().body(request).post(BASE_URL + "/rooms/{roomId}/players")
//                .then().extract().response();
//
//        assertThat(response.statusCode(), is(equalTo(400)));
//    }
//
//    // GET Player cards
//    @Test
//    public void getPlayerCards200() throws IOException {
//        // Room
//        String roomFilePath = JSON_REQUESTS_PATH + "/postRoom.json";
//        String roomInputName = "room_2";
//        String roomRequest = jsonUtil.createPostRoomJson(roomInputName, "false", roomFilePath);
//
//        String roomId = given().contentType(ContentType.JSON)
//                .when().body(roomRequest).post(BASE_URL + "/rooms")
//                .then().extract().response().path("uid");
//
//        runningRoomIds.add(roomId);
//
//        // Player
//        String playerFilePath = JSON_REQUESTS_PATH + "/postPutPlayer.json";
//        String playerInputName = "player_2";
//        String playerRequest = jsonUtil.createPostPutPlayerJson(playerInputName, playerFilePath);
//
//        String playerId = given().contentType(ContentType.JSON).pathParam("roomId", roomId)
//                .when().body(playerRequest).post(BASE_URL + "/rooms/{roomId}/players")
//                .then().extract().response().path("pid");
//
//        // Request
//        Response response = given().pathParam("uid", roomId).pathParam("pid", playerId)
//                .when().get(BASE_URL + "/rooms/{uid}/players/{pid}/cards")
//                .then().extract().response();
//
//        assertThat(response.statusCode(), is(equalTo(200)));
//    }
//}
