package com.learning.uwuno.controllers;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.learning.uwuno.player;
import com.learning.uwuno.room;
import com.learning.uwuno.errors.errorNotFound;
import com.learning.uwuno.services.roomContainerService;

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
        if (StringUtils.isNumeric(uid)) {
            try {
                room room = containerService.getRoom(Integer.parseInt(uid));
                return room.getPlayers();
            } catch (NoSuchElementException e) {
                throw new errorNotFound();
            }
        }
        throw new errorNotFound();
    }

    // POSTS
    // Adds a new player to the given room uid.
    @PostMapping(value = "rooms/{uid}/players")
    public void addPlayer(@RequestBody player newPlayer, @PathVariable String uid) {
        if (StringUtils.isNumeric(uid)) {
            try {
                // TODO: Check if name already exists
                // TODO: Generate unique id
                room room = containerService.getRoom(Integer.parseInt(uid));
                room.addPlayer(newPlayer);
            } catch (NoSuchElementException e) {
                throw new errorNotFound();
            }
        } else {
            throw new errorNotFound();
        }
    }

    // PUTS
    // Update player name in given room uid
    @PutMapping(value = "rooms/{uid}/players")
    public void updatePlayerName(@RequestBody String json, @PathVariable String uid)
            throws JsonMappingException, JsonProcessingException {
        if (StringUtils.isNumeric(uid)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                // TODO: Check if name already exists
                room room = containerService.getRoom(Integer.parseInt(uid));
                Map<String, String> map = mapper.readValue(json, new TypeReference<Map<String, String>>(){});
                player player = room.getPlayer(Integer.parseInt(map.get("pid")));
                player.setName(map.get("newName"));
            }
            catch (NoSuchElementException e) {
                throw new errorNotFound();
            }
        } else {
            throw new errorNotFound();
        }
    }

    // DELETES
    @DeleteMapping(value = "rooms/{uid}/players")
    public void deletePlayer(@RequestBody String json, @PathVariable String uid)
            throws JsonMappingException, JsonProcessingException {
        if (StringUtils.isNumeric(uid)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                room room = containerService.getRoom(Integer.parseInt(uid));
                Map<String, String> map = mapper.readValue(json, new TypeReference<Map<String, String>>(){});
                room.deletePlayer(Integer.parseInt(map.get("pid")));
            }
            catch (NoSuchElementException e) {
                throw new errorNotFound();
            }
        }
        else {
            throw new errorNotFound();
        }
    }
}