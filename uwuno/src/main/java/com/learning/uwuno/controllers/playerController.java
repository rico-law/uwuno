package com.learning.uwuno.controllers;

import com.learning.uwuno.*;
import com.learning.uwuno.cards.card;
import com.learning.uwuno.errors.*;
import com.learning.uwuno.services.gameService;
import com.learning.uwuno.util.parser;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@RestController
public class playerController {
    // Runs and injects container service
    @Autowired
    private gameService containerService;

    // POSTS
    // Adds a new player to the given room uid.
    @PostMapping(value = "rooms/{uid}/players")
    public String addPlayer(@RequestBody String json, @PathVariable String uid) {
        try {
            // OPTIONAL TODO: Check if name already exists and append number to it
            parser parser = new parser(json);
            return containerService.addPlayer(parser.getValue("name"), uid);
        }
        catch (NoSuchElementException e) {
            throw new errorNotFound();
        }
        catch (JsonProcessingException e) {
            throw new internalServerError();
        }
    }

    // GETS
    // Return list of card available to player
    @GetMapping(value = "rooms/{uid}/players/{pid}/cards")
    public ArrayList<card> getPlayerCards(@PathVariable String uid, @PathVariable String pid) {
        return containerService.getPlayer(uid, pid).getCardList();
    }

    // PUTS
    // Possible calls for player PUT, for differing json inputs check JSON Documentation.txt
    // 1) Update player name
    // 2) Play Card
    // 3) Draw Card
    @PutMapping(value = "rooms/{uid}/players/{pid}")
    public Object handlePlayerPuts(@RequestBody String json, @PathVariable String uid, @PathVariable String pid) {
        parser parser;
        try {
            parser = new parser(json);
        }
        catch (JsonProcessingException e) {
            throw new internalServerError();
        }

        // Handle updating name
        if (parser.exists("name")) {
            try { // TODO: Check if name already exists and append number to it
                containerService.updatePlayerName(uid, pid, parser.getValue("name"));
                // Should we do it this way? Def good for debugging front end at least
                return containerService.getPlayer(uid, pid);
            }
            catch (NoSuchElementException e) {
                throw new errorNotFound();
            }
        }
        // Handle drawing a card - add to a player's cardList
        else if (parser.exists("draw")) {
            try {
                int numToDraw = Integer.parseInt(parser.getValue("draw"));
                return containerService.drawCards(uid, pid, numToDraw);
            }
            catch (NumberFormatException e) {
                throw new badRequest();
            }
        }
        // Handle playing a card - remove card from player and add to deck
        // Will check for whether card is playable, however will throw if not
        else if (parser.exists("cardType") &&
                parser.exists("cardColor") &&
                parser.exists("cardValue") &&
                parser.exists("wildColor")) {
            containerService.playCard(uid, pid, parser.getValue("cardType"),
                                                parser.getValue("cardColor"),
                                                parser.getValue("cardValue"),
                                                parser.getValue("wildColor"));
            return containerService.getPlayer(uid, pid).getCardList();
        }
        // Handle missing card information for playing
        else if (parser.exists("cardType") ||
                parser.exists("cardColor") ||
                parser.exists("cardValue") ||
                parser.exists("wildColor")) {
            throw new badRequest(); // Separate for custom message later on?
        }
        else {
            throw new badRequest();
        }
    }

    // DELETES
    @DeleteMapping(value = "rooms/{uid}/players/{pid}")
    public void deletePlayer(@RequestBody String json, @PathVariable String uid, @PathVariable String pid) {
        try {
            containerService.deletePlayer(uid, pid);
        }
        catch (NoSuchElementException e) {
            throw new errorNotFound();
        }
    }
}
