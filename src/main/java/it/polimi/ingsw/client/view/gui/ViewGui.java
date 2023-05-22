package it.polimi.ingsw.client.view.gui;
import it.polimi.ingsw.client.view.View;
import javafx.application.Platform;

public class ViewGui extends View {
    private final String startingScene;
    public ViewGui(String startingScene) {
        super();
        this.startingScene = startingScene;
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
    public void handleServerConnectionError(String message) {
        GuiController controller = SceneManager.getCurrentController();
        Platform.runLater(()->controller.showServerConnectionError(message));
    }

    @Override
    public void handleWinnerSelected(String winner) {

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
        GuiController.setView(this);
        GUI.main(new String[]{startingScene});
    }




}
