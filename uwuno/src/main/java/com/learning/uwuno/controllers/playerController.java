package com.learning.uwuno.controllers;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import com.learning.uwuno.player;
import com.learning.uwuno.room;
import com.learning.uwuno.errors.errorNotFound;
import com.learning.uwuno.services.roomContainerService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
            }
            catch (NoSuchElementException e) {
                throw new errorNotFound();
            }
        }
        throw new errorNotFound();
    }

    // POSTS
    // Adds a new player to the given room uid.
    @PostMapping(value = "rooms/{uid}/players")
    public boolean addPlayer(@RequestBody player newPlayer, @PathVariable String uid) {
        if (StringUtils.isNumeric(uid)) {
            try {
                room room = containerService.getRoom(Integer.parseInt(uid));
                return room.addPlayer(newPlayer);
            }
            catch (NoSuchElementException e) {
                throw new errorNotFound();
            }
        }
        throw new errorNotFound();
    }

    // PUTS
    // @PutMapping(value = "rooms/{uid}/players")
    // public void updatePlayerName(@RequestBody ) {

    // }

    // // DELETES

}