package com.learning.uwuno.controllers;

import com.learning.uwuno.*;
import com.learning.uwuno.services.gameService;
import com.learning.uwuno.errors.*;
import com.learning.uwuno.util.parser;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/*
All controller requests related to game rooms should go here
Entity Classes:
    roomContainer
    room
*/

@RestController
public class gameRoomController {
    // Runs and injects container service
    @Autowired
    private gameService containerService;

    // POSTS
    @PostMapping(value = "rooms")
    public ResponseEntity<room> addRoom(@RequestBody String json) {
        try {
            parser parser = new parser(json);
            if (parser.exists("roomName") && parser.exists("useBlankCards")) {
                return ResponseEntity.ok(containerService.addRoom(parser.getValue("roomName"),
                        Boolean.parseBoolean(parser.getValue("useBlankCards"))));
            }
            throw new badRequest();
        }
        catch (JsonProcessingException e) {
            throw new internalServerError();
        }
    }

    // GETS
    // Returns .json formatted vector of rooms, private variables are shown (room name)
    @GetMapping(value = "rooms")
    public ResponseEntity<ArrayList<room>> rooms() {
        return ResponseEntity.ok(containerService.getRoomList()); // TODO: Fix to not return player pids
    }

    // Example: localhost:8080/rooms/2
    @GetMapping(value = "rooms/{uid}")
    public ResponseEntity<room> getRoom(@PathVariable String uid) {
        try {
            return ResponseEntity.ok(containerService.getRoom(uid));
        } catch (NoSuchElementException e) {
            throw new errorNotFound();
        }
    }

    // Returns a list of players in given room uid.
    @GetMapping(value = "rooms/{uid}/players")
    public ResponseEntity<ArrayList<player>> getPlayers(@PathVariable String uid) {
        try {
            room room = containerService.getRoom(uid);
            return ResponseEntity.ok(room.getPlayers());
        }
        catch (NoSuchElementException e) {
            throw new errorNotFound();
        }
    }

    // PUTS
    @PutMapping(value = "rooms/{uid}")
    public ResponseEntity<Void> updateRoomName(@PathVariable String uid, @RequestBody String json) {
        try {
            parser parser = new parser(json);
            containerService.updateRoomName(uid, parser.getValue("roomName"));
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (NoSuchElementException e) {
            throw new errorNotFound();
        }
        catch (IOException e) {
            throw new badRequest();
        }
    }

    // DELETES
    // TODO: Figure out how to prevent ppl from deleting a room that isn't theirs (maybe make a hidden password per room?
    @DeleteMapping(value = "rooms/{uid}")
    public ResponseEntity<Void> deleteRoom(@PathVariable String uid) {
        try {
            containerService.deleteRoom(uid);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        catch (NoSuchElementException e) {
            throw new errorNotFound();
        }
    }
}



