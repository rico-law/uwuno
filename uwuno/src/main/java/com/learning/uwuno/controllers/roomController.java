package com.learning.uwuno.controllers;

import com.learning.uwuno.*;
import com.learning.uwuno.errors.badRequest;
import com.learning.uwuno.game.gameSettings;
import com.learning.uwuno.services.gameService;
import com.learning.uwuno.util.parser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;

/*
All controller requests related to game rooms should go here
Entity Classes:
    roomContainer
    room
*/

@RestController
public class roomController {
    // Runs and injects container service
    @Autowired
    private gameService containerService;

    // POSTS
    @PostMapping(value = "rooms")
    public ResponseEntity<room> addRoom(@RequestBody String json) {
            parser parser = new parser(json);
            return ResponseEntity.ok(containerService.addRoom(parser.getValue("roomName"),
                        Boolean.parseBoolean(parser.getValue("useBlankCards"))));
    }

    // GETS
    // Returns .json formatted vector of rooms, private variables are shown (room name)
    @GetMapping(value = "rooms")
    public ResponseEntity<ArrayList<room>> getAllRooms() {
        return ResponseEntity.ok(containerService.getRoomList()); // TODO: Fix to not return player pids
    }

    // Example: localhost:8080/rooms/2
    @GetMapping(value = "rooms/{uid}")
    public ResponseEntity<room> getRoom(@PathVariable String uid) {
        return ResponseEntity.ok(containerService.getRoom(uid));
    }

    // Returns a list of players in given room uid.
    @GetMapping(value = "rooms/{uid}/players")
    public ResponseEntity<LinkedList<player>> getPlayers(@PathVariable String uid) {
        return ResponseEntity.ok(containerService.getRoom(uid).getPlayers());
    }

    /**
     * Return gameSettings for given room uid.
     * @param uid: room uid
     * @return ResponseEntity
     */
    @GetMapping(value = "rooms/{uid}/settings")
    public ResponseEntity<gameSettings> getGameSettings(@PathVariable String uid) {
        return ResponseEntity.ok(containerService.getGameSettings(uid));
    }

    // PUTS
    /**
     * PUT requests for setting roomName, roomStatus.
     * @param uid: room uid
     * @param json: gameSettings jsons
     * @return ResponseEntity
     */
    @PutMapping(value = "rooms/{uid}")
    public ResponseEntity<room> updateRoom(@PathVariable String uid, @RequestBody String json) {
        parser parser = new parser(json);
        if (parser.exists("roomName") && parser.exists("roomStatus")) {
            containerService.updateRoomName(uid, parser.getValue("roomName"));
            containerService.updateRoomStatus(uid, parser.getValue("roomStatus"));
        } else {
            throw new badRequest("Unknown Room Modification");
        }
        return ResponseEntity.ok(containerService.getRoom(uid));
    }

    // DELETES
    // TODO: Figure out how to prevent ppl from deleting a room that isn't theirs (maybe make a hidden password per room?
    @DeleteMapping(value = "rooms/{uid}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String uid) {
        containerService.deleteRoom(uid);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // GameSettings PUT and POST
    /**
     * POST and PUT requests for setting gameSettings.
     * @param uid: room uid
     * @param json: gameSettings jsons
     * @return ResponseEntity
     * TODO: pass through an object rather than each parsed value
     */
    @RequestMapping(value = "rooms/{uid}/settings", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<room> setGameSettings(@PathVariable String uid, @RequestBody String json) {
        parser parser = new parser(json);
        if (parser.exists("gameMode") && parser.exists("maxTurn")
                && parser.exists("maxScore") && parser.exists("useBlankCards")) {
            containerService.updateGameSettings(uid, parser.getValue("gameMode"),
                    Integer.parseInt(parser.getValue("maxTurn")), Integer.parseInt(parser.getValue("maxScore")),
                    Boolean.parseBoolean(parser.getValue("useBlankCards")));
        } else {
            throw new badRequest("Unknown Game Settings Modification");
        }
        return ResponseEntity.ok(containerService.getRoom(uid));
    }
}



