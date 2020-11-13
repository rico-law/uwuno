package com.learning.uwuno.game;

import com.learning.uwuno.errors.badRequest;
import com.learning.uwuno.game.gameModes.gameMode;
import com.learning.uwuno.game.gameModes.normalMode;
import com.learning.uwuno.game.gameModes.pointMode;

public class gameSettings {
    private gameMode gameMode;
    private int maxTurn;
    private int maxScore;
    private boolean useBlankCards;

    // TODO: may want to make a controller for gameSettings
    public gameMode getGameMode() {
        return gameMode;
    }

    public int getMaxTurn() {
        return maxTurn;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public boolean isUseBlankCards() {
        return useBlankCards;
    }

    public void setGameMode(String mode) {
        this.gameMode = createGameMode(mode);
    }

    public void setMaxTurn(int maxTurn) {
        this.maxTurn = maxTurn;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public void setUseBlankCards(boolean useBlankCards) {
        this.useBlankCards = useBlankCards;
    }

    private gameMode createGameMode(String mode) {
        return switch (mode.toLowerCase()) {
            case "normal" -> new normalMode();
            case "point" -> new pointMode();
            default -> throw new badRequest("Requesting invalid game mode");
        };
    }
}
