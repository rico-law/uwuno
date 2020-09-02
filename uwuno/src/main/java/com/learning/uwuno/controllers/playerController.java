package com.learning.uwuno.controllers;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.learning.uwuno.*;
import com.learning.uwuno.errors.*;
import com.learning.uwuno.services.roomContainerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
            parser parser = new parser(json);
            room room = containerService.getRoom(uid);
            player newPlayer = new player(parser.getValue("name"));
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
            parser parser = new parser(json);
            room room = containerService.getRoom(uid);
            player player = room.getPlayer(parser.getValue("id"));
            player.setName(parser.getValue("name"));
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
            parser parser = new parser(json);
            room room = containerService.getRoom(uid);
            room.deletePlayer(parser.getValue("id"));
        }
        catch (NoSuchElementException e) {
            throw new errorNotFound();
        }
        catch (JsonProcessingException e) {
            throw new internalServerError();
        }
    }
}