package it.polimi.ingsw.client.view.gui;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.gui.controllers.*;
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
    public void handleSuccessMessage(String message) {
        GuiController controller = SceneManager.getCurrentController();
        switch (message) {
            case "NAME" -> {
                if (controller instanceof SubMenuController)
                    Platform.runLater(() -> ((SubMenuController) controller).confirmUsername());
                else
                    throw new RuntimeException("Wrong GUI state");
            }
            case "Successfully loaded game" -> {
                if (controller instanceof MenuLoadGameController)
                    Platform.runLater(() -> ((MenuLoadGameController) controller).loadGame());
                else
                    throw new RuntimeException("Wrong GUI state");
            }
        }
    }

    @Override
    public void handleErrorMessage(String message) {
        GuiController controller = SceneManager.getCurrentController();
        if (message.equals("Another user has chosen this name")) {

            if (controller instanceof SubMenuController)
                Platform.runLater(() -> ((SubMenuController) controller).notifyNameAlreadyTaken());
            else
                throw new RuntimeException("Wrong GUI state");

        } else if (message.contains("You are not whitelisted in this game")) {

            if (controller instanceof MenuLoadGameController)
                Platform.runLater(() -> ((MenuLoadGameController) controller).notifyNotWhitelisted());
            else
                throw new RuntimeException("Wrong GUI state");

        }
    }

    @Override
    public void updateGameStatus(boolean isGameOver) {
        if(isGameOver){
            //NOTIFY GUI
        }
    }

    @Override
    public void handleWinnerSelected(String winner) {
        //NOTIFY GUI
    }

    @Override
    public void updatePlayerScore(String player, int score) {
        //NOTIFY GUI
    }

    @Override
    public void blockUsersGameCommands() {
        //Not my turn
        GuiController controller = SceneManager.getCurrentController();
        if (controller instanceof BoardController)
            Platform.runLater(()->((BoardController)controller).blockCommands());
        else
            throw new RuntimeException("Wrong GUI state");
    }

    @Override
    public void allowUsersGameCommands() {
        GuiController controller = SceneManager.getCurrentController();
        if (controller instanceof BoardController)
            Platform.runLater(()->((BoardController)controller).unblockCommands());
        else
            throw new RuntimeException("Wrong GUI state");
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
        GuiController controller = SceneManager.getCurrentController();
        if (controller instanceof BoardController)
            Platform.runLater(()->((BoardController)controller).updatePoints(cardId, points));
        else
            throw new RuntimeException("Wrong GUI state");
    }

    @Override
    public void setCommonGoal(int id, int points) {
        GuiController controller = SceneManager.getCurrentController();
        if (controller instanceof BoardController)
            Platform.runLater(()->((BoardController)controller).setCommonGoalCard(id, points));
        else
            throw new RuntimeException("Wrong GUI state");
    }

    @Override
    public void setPersonalGoal(int id) {
        GuiController controller = SceneManager.getCurrentController();
        if (controller instanceof BoardController)
            Platform.runLater(()->((BoardController)controller).setPersonalGoalCard(id));
        else
            throw new RuntimeException("Wrong GUI state");
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
    public void reset() {}
    @Override
    public void finalizeUpdate() {}
}
