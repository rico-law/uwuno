package com.learning.uwuno.controllers;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.learning.uwuno.player;
import com.learning.uwuno.room;
import com.learning.uwuno.errors.*;
import com.learning.uwuno.services.roomContainerService;
import com.learning.uwuno.util.requestUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
public class playerController {
    // Runs and injects container service
    @Autowired
    private roomContainerService containerService;

    // GETS
    // Returns a list of players in given room uid.
    @GetMapping(value = "rooms/{uid}/players")
    public ArrayList<player> players(@PathVariable String uid) {
        try {
            room room = containerService.getRoom(uid);
            return room.getPlayers();
        }
        catch (NoSuchElementException e) {
            throw new errorNotFound();
        }
    }

    // POSTS
    // Adds a new player to the given room uid.
    @PostMapping(value = "rooms/{uid}/players")
    public String addPlayer(@RequestBody String json, @PathVariable String uid)
            throws JsonMappingException, JsonProcessingException {
        try {
            // OPTIONAL TODO: Check if name already exists and append number to it
            Map<String, String> map = requestUtil.parseJson(json);
            room room = containerService.getRoom(uid);
            player newPlayer = new player(map.get("name"));
            room.addPlayer(newPlayer);
            return newPlayer.getUid();
        }
        catch (NoSuchElementException e) {
            throw new errorNotFound();
        }
        catch (JsonProcessingException e) {
            throw new internalServerError();
        }
    }

    // PUTS
    // Update player name in given room uid
    @PutMapping(value = "rooms/{uid}/players")
    public void updatePlayerName(@RequestBody String json, @PathVariable String uid)
            throws JsonMappingException, JsonProcessingException {
        try {
            // OPTIONAL TODO: Check if name already exists and append number to it
            Map<String, String> map = requestUtil.parseJson(json);
            room room = containerService.getRoom(uid);
            player player = room.getPlayer(map.get("id"));
            player.setName(map.get("name"));
        }
        catch (NoSuchElementException e) {
            throw new errorNotFound();
        }
        catch (JsonProcessingException e) {
            throw new internalServerError();
        }
    }

    // DELETES
    @DeleteMapping(value = "rooms/{uid}/players")
    public void deletePlayer(@RequestBody String json, @PathVariable String uid)
            throws JsonMappingException, JsonProcessingException {
        try {
            Map<String, String> map = requestUtil.parseJson(json);
            room room = containerService.getRoom(uid);
            room.deletePlayer(map.get("id"));
        }
        catch (NoSuchElementException e) {
            throw new errorNotFound();
        }
        catch (JsonProcessingException e) {
            throw new internalServerError();
        }
    }
}