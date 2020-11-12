package com.learning.uwuno.game.gameModes;

public class pointMode implements gameMode {
    final private String NAME = "POINT";

    @Override
    public String getModeName() {
        return NAME;
    }

    @Override
    public void calculateScore() {

    }

    @Override
    public String determineWinner() {
        return "";
    }
}
