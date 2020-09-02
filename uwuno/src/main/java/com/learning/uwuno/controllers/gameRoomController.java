package com.learning.uwuno.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.uwuno.errors.badRequest;
import com.learning.uwuno.room;
import com.learning.uwuno.services.roomContainerService;
import com.learning.uwuno.util.requestUtil;
import com.learning.uwuno.errors.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Map;

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
    private roomContainerService containerService;

    // GETS
    // Returns .json formatted vector of rooms, private variables are shown (room
    // name)
    @GetMapping(value = "rooms")
    public ArrayList<room> rooms() {
        return containerService.getRoomList();
    }

    // Example: localhost:8080/rooms/2
    @GetMapping(value = "rooms/{uid}")
    public room getRoom(@PathVariable String uid) {
        try {
            return containerService.getRoom(uid);
        } catch (NoSuchElementException e) {
            throw new errorNotFound();
        }
    }

    // POSTS
    @PostMapping(value = "rooms")
    public String addRoom(@RequestBody String json) throws JsonMappingException, JsonProcessingException {
        try {
            Map<String, String> map = requestUtil.parseJson(json);
            room newRoom = new room(map.get("roomName"));
            containerService.addRoom(newRoom);
            return newRoom.getUid();
        }
        catch (JsonProcessingException e) {
            throw new internalServerError();
        }
    }

    // PUTS
    @PutMapping(value = "rooms/{uid}")
    public void updateRoomName(@PathVariable String uid, @RequestBody String json) {
        try {
            Map<String, String> map = requestUtil.parseJson(json);
            containerService.updateRoomName(uid, map.get("roomName"));
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
    public void deleteRoom(@PathVariable String uid) {
        try {
            containerService.deleteRoom(uid);
        }
        catch (NoSuchElementException e) {
            throw new errorNotFound();
        }
    }
}



