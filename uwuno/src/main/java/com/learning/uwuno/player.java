package com.learning.uwuno;

public class player {
    // Class Variables
    final private int uid;
    private String name;

    public player(int id, String name) {
        this.uid = id;
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
