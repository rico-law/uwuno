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

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        // Create room
        String filePath = JSON_REQUESTS_PATH + "/postRoom.json";
        String request = jsonUtil.createPostRoomJson("room", "false", filePath);

        roomId = given().contentType(ContentType.JSON)
                .when().body(request).post(BASE_URL + "/rooms")
                .then().extract().response().path("uid");
    }

    @AfterEach
    public void cleanUp() {
        // Delete room
        given().pathParam("uid", roomId)
                .when().delete(BASE_URL + "/rooms/{uid}");
    }

    @Test
    public void putValidRoom200() throws IOException {
        // TODO: check room status changed
    }
//        // Room
//        String oldFilePath = JSON_REQUESTS_PATH + "/postRoom.json";
//        String oldInputName = "room_put_old_name_valid";
//        String oldRequest = jsonUtil.createPostRoomJson(oldInputName, "false", oldFilePath);
//
//        String roomId = given().contentType(ContentType.JSON)
//                .when().body(oldRequest).post(BASE_URL + "/rooms")
//                .then().extract().response().path("uid");
//
//        runningRoomIds.add(roomId);
//
//        // Request
//        String filePath = JSON_REQUESTS_PATH + "/putRoom.json";
//        String inputName = "room_put_new_name_valid_200";
//        String status = "Start";
//        String request = jsonUtil.createPutRoomJson(inputName, roomId, status, filePath);
//
//        Response response = given().pathParam("uid", roomId)
//                .when().body(request).put(BASE_URL + "/rooms/{uid}")
//                .then().extract().response();
//
//        Response getRequest = given().pathParam("uid", roomId)
//                .when().get(BASE_URL + "/rooms/{uid}")
//                .then().extract().response();
//
//        assertThat(response.statusCode(), is(equalTo(200)));
//        assertThat(getRequest.path("roomStatus"), is(equalTo(status)));
//    }
}
