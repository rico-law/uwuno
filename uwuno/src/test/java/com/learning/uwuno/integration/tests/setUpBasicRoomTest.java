package com.learning.uwuno.integration.tests;

import com.learning.uwuno.integration.jsonUtil;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static com.learning.uwuno.integration.constants.*;
import static io.restassured.RestAssured.*;

public class setUpBasicRoomTest {
    @Test
    public void createValidPlayerAndRoom() throws FileNotFoundException {
        String roomFile = jsonUtil.jsonToString(JSON_REQUESTS_PATH + "/postRoomNoBlank.json");
        String playerFile = jsonUtil.jsonToString(JSON_REQUESTS_PATH + "/postPlayer.json");

        Response response = given().contentType(ContentType.JSON)
                .when().body(roomFile).post(BASE_URL + "/rooms")
                .then().extract().response();

        System.out.println("request: " + roomFile);
        System.out.println("response: " + response.asString());
    }
}
