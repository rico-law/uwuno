package com.learning.uwuno.util;

import com.learning.uwuno.errors.internalServerError;
import com.learning.uwuno.player;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

// Container class for a list of players
public class playerList extends LinkedList<player> {
    // Class Variables
    private final String parentUID;
    // Ensure that this HashSet and the LinkedList is kept in a consistent state
    private HashSet<String> playerNames;

    // Class functions
    public playerList(String uid) {
        parentUID = uid;
        playerNames = new HashSet<String>();
    }

    // Only Call this function from within this.add(), all the checking code is there
    private boolean addPlayerToSet(player newPlayer) {
        // TODO: Make this more robust, there should be some way to revert back to a valid state in the future
        // ie. super.add() can return true, but not playerNames.add() which leaves the object in an inconsistent state
        return super.add(newPlayer) && playerNames.add(newPlayer.getName());
    }

    public player changeName(player player, String newName) {
        String revisedName = validateAndReturnNewName(newName);
        if (!playerNames.remove(player.getName()))
            throw new internalServerError("Previous name does not exists in hash set");
        playerNames.add(revisedName);
        player.setName(revisedName);
        return player;
    }

    // Checks name against current list of names and returns unique name (ie. adds hash to name if name already in use)
    private String validateAndReturnNewName(String name) {
        String newName = name;
        if (containsName(name)) {
            boolean isNameInUse = true;
            while (isNameInUse) {
                String hash = "#" + String.valueOf((Math.random() * 9999) + 1000).substring(0, 4);
                newName = name + hash;
                isNameInUse = containsName(newName);
            }
        }
        return newName;
    }

    // Checks HashSet and not the LinkedList itself
    public boolean containsName(String name) {
        return playerNames.contains(name);
    }

    // Override Functions
    // Need to reimplement some of the base LinkedList functions, leaving some to throw
    // as we do not need them, can fill them out when needed
    @Override
    public boolean add(player newPlayer) {
        if (this.contains(newPlayer))
            throw new internalServerError("Attempted to add a player to a room more than once");
        // Don't allow two names to be the same, if newPlayer's name is already taken add a hash to end of name
        if (containsName(newPlayer.getName())) {
            String newName = "";
            boolean isNameInUse = true;
            while (isNameInUse) {
                String hash = "#" + String.valueOf((Math.random() * 9999) + 1000).substring(0, 4);
                newName = newPlayer.getName() + hash;
                isNameInUse = containsName(newName);
            }
            newPlayer.setName(newName);
        }
        return addPlayerToSet(newPlayer);
    }

    @Override
    public void add(int index, player element) {
        throw new internalServerError("add(int index, player element) is not supported");
    }

    @Override
    public boolean addAll(Collection<? extends player> c) {
        throw new internalServerError("addAll is not supported");
    }

    @Override
    public boolean addAll(int index, Collection<? extends player> c) {
        throw new internalServerError("addAll is not supported");
    }

    @Override
    public void addFirst(player player) {
        throw new internalServerError("addFirst is not supported");
    }

    @Override
    public void addLast(player player) {
        throw new internalServerError("addLast is not supported");
    }

    @Override
    public void clear() {
        super.clear();
        playerNames.clear();
    }

    @Override
    public player poll() {
        player tmp = super.poll();
        playerNames.remove(tmp.getName());
        return tmp;
    }

    @Override
    public player pollFirst() {
        throw new internalServerError("pollFirst is not supported");
    }

    @Override
    public player pollLast() {
        throw new internalServerError("pollLast is not supported");
    }

    @Override
    public boolean offer(player player) {
        throw new internalServerError("offer is not supported");
    }

    @Override
    public boolean offerFirst(player player) {
        throw new internalServerError("offerFirst is not supported");
    }

    @Override
    public boolean offerLast(player player) {
        throw new internalServerError("offerLast is not supported");
    }

    @Override
    public player pop() {
        player tmp = super.pop();
        playerNames.remove(tmp.getName());
        return tmp;
    }

    @Override
    public void push(player player) {
        add(player);
    }

    @Override
    public player remove() {
        player tmp = super.remove();
        playerNames.remove(tmp.getName());
        return tmp;
    }

    @Override
    public player remove(int index) {
        throw new internalServerError("remove(int index) is not supported");
    }

    @Override
    public boolean remove(Object o) {
        if (!this.isEmpty()) {
            if (this.get(0).getClass() == o.getClass()) {
                playerNames.remove(((player) o).getName());
            }
            else {
                throw new internalServerError("Object to remove is of wrong class");
            }
        }
        return super.remove(o);
    }

    @Override
    public player removeFirst() {
        throw new internalServerError("removeFirst is not supported");
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new internalServerError("removeFirstOccurrence is not supported");
    }

    @Override
    public player removeLast() {
        throw new internalServerError("removeLast is not supported");
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new internalServerError("removeLastOccurrence is not supported");
    }

    @Override
    public player set(int index, player element) {
        throw new internalServerError("set is not supported");
    }

    // TODO: Need to fix this, but only way to do this seems to be to return a new playerList object?
    @Override
    public Object clone() {
        throw new internalServerError("clone is not supported");
    }
}
