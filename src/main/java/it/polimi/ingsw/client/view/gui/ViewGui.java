package it.polimi.ingsw.client.view.gui;
import it.polimi.ingsw.client.view.gui.controllers.SplashScreenController;
import javafx.application.Platform;
import javafx.stage.Stage;
import it.polimi.ingsw.client.view.View;

public class ViewGui extends View {
    private String startingView;
    public ViewGui(String startingView) {
        super();
        this.startingView = startingView;

    }

    @Override
    public void reset() {

    }

    @Override
    public void finalizeUpdate() {

    }

    @Override
    public void showListOfSavedGames(String[] savedGames) {

    }

    @Override
    public void showServerConnectionError() {

    }

    @Override
    public void handleSuccessMessage(String message) {

    }

    @Override
    public void handleErrorMessage(String message) {

    }

    @Override
    public void updateGameStatus(boolean isGameOver) {

    }

    @Override
    public void updatePlayerScore(String player, int score) {

    }

    @Override
    public void updateBookshelf(String player, int[][] update) {

    }

    @Override
    public void updateBoard(int[][] update) {

    }

    @Override
    public void updatePlayerConnectionStatus(String player, boolean isDisconnected) {

    }

    @Override
    public void updateCommonGoalPoints(int cardId, int points) {

    }

    @Override
    public void setCommonGoal(int id, int points) {

    }

    @Override
    public void setPersonalGoal(int id) {

    }

    @Override
    public void setCurrentTurn(int turn) {

    }

    @Override
    public void addMessage(String message, String sender, boolean isWhisper) {

    }

    @Override
    public void addPlayer(String username, int score, boolean isClient) {

    }

    @Override
    public void run() {
        GUI.main(new String[]{startingView});
    }

}
