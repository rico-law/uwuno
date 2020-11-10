package com.learning.uwuno.integration;

import io.micrometer.core.instrument.util.IOUtils;
import org.junit.jupiter.api.DisplayNameGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class jsonUtil {
    private static final String ROOM_NAME = "$roomName";
    private static final String ROOM_UID = "$roomUid";
    private static final String ROOM_STATUS = "$roomStatus";
    private static final String BLANK_CARDS = "$useBlankCards";
    private static final String PLAYER_NAME = "$playerName";
    private static final String NUM_CARDS = "$numCards";
    private static final String CARD_TYPE = "$cardType";
    private static final String CARD_COLOR = "$cardColor";
    private static final String CARD_VALUE = "$cardValue";
    private static final String SET_WILD_COLOR = "$setWildCard";
    private static final String SKIP = "$skip";

    private jsonUtil() {
        // Should not run
    }
    public static String jsonFileToString(String jsonPath) throws FileNotFoundException {
        File file = new File(jsonPath);
        FileInputStream inputStream = new FileInputStream(file);
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

    // Create Room JSON String
    public static String createPostRoomJson(String roomName, String useBlank,String jsonPath)
            throws FileNotFoundException {
        return jsonFileToString(jsonPath).replace(ROOM_NAME, roomName).replace(BLANK_CARDS, useBlank);
    }

    public static String createPutRoomJson(String roomName, String uid, String status, String jsonPath)
            throws FileNotFoundException {
        return jsonFileToString(jsonPath).replace(ROOM_NAME, roomName).replace(ROOM_UID, uid)
                .replace(ROOM_STATUS, status);
    }

    // Create Player JSON String
    public static String createPostPutPlayerJson(String playerName, String jsonPath) throws FileNotFoundException {
        return jsonFileToString(jsonPath).replace(PLAYER_NAME, playerName);
    }

    public static String createPutPlayerDrawCardJson(String numCards, String jsonPath) throws FileNotFoundException {
        return jsonFileToString(jsonPath).replace(NUM_CARDS, numCards);
    }

    public static String createPutPlayerPlayCardJson(String cardType, String cardColor, String cardValue,
                                                     String setWildColor,String skip, String jsonPath)
            throws FileNotFoundException {
        return jsonFileToString(jsonPath).replace(CARD_TYPE, cardType).replace(CARD_COLOR, cardColor)
                .replace(CARD_VALUE, cardValue).replace(SET_WILD_COLOR, setWildColor).replace(SKIP, skip);
    }
}
