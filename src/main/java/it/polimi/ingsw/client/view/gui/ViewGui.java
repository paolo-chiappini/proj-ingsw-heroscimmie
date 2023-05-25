package it.polimi.ingsw.client.view.gui;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.gui.controllers.MenuLoadGameController;
import it.polimi.ingsw.client.view.gui.controllers.MenuWaitGameController;
import it.polimi.ingsw.client.view.gui.controllers.SubMenuController;
import it.polimi.ingsw.client.view.gui.controllers.boardview.BoardController;
import javafx.application.Platform;

public class ViewGui extends View {
    private final String startingScene;

    public ViewGui(String startingScene) {
        super();
        this.startingScene = startingScene;
    }

    @Override
    public void startGameView(Runnable finishSetup) {
        GuiController controller = SceneManager.getCurrentController();
        if (controller instanceof MenuWaitGameController)
            Platform.runLater(()->{
                ((MenuWaitGameController)controller).startGame();
                finishSetup.run();
            });
        else
            throw new RuntimeException("Wrong GUI state");
    }

    @Override
    public void finalizeUpdate() {

    }

    @Override
    public void showListOfSavedGames(String[] savedGames) {
        GuiController controller = SceneManager.getCurrentController();
        if(controller instanceof MenuLoadGameController){
            ((MenuLoadGameController)controller).populateList(savedGames);
        }else {
            throw new RuntimeException("Wrong GUI state");
        }
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
        if(message.equals("NAME")){
            GuiController controller = SceneManager.getCurrentController();
            if (controller instanceof SubMenuController)
                Platform.runLater(()->((SubMenuController) controller).joinGame());
            else
                throw new RuntimeException("Wrong GUI state");
        }
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
        GuiController controller = SceneManager.getCurrentController();
        if (controller instanceof BoardController)
            Platform.runLater(()->((BoardController)controller).updateBookshelf(update, player));
        else
            throw new RuntimeException("Wrong GUI state");
    }

    @Override
    public void updateBoard(int[][] update) {
        GuiController controller = SceneManager.getCurrentController();
        if (controller instanceof BoardController)
            Platform.runLater(()->((BoardController)controller).updateBoard(update));
        else
            throw new RuntimeException("Wrong GUI state");
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
        GuiController controller = SceneManager.getCurrentController();
        if (controller instanceof BoardController)
            Platform.runLater(()->((BoardController)controller).addPlayer(username, score, isClient));
        else
            throw new RuntimeException("Wrong GUI state");
    }

    @Override
    public void run() {
        GuiController.setView(this);
        GUI.main(new String[]{startingScene});
    }

    @Override
    public void shutdown() {
        super.shutdown();
        SceneManager.getCurrentController().shutdown();
    }

    @Override
    public void reset() {

    }


}
