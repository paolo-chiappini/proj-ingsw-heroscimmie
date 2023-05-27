package it.polimi.ingsw.client.view.gui;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.gui.controllers.*;
import it.polimi.ingsw.client.view.gui.controllers.boardview.BoardController;
import javafx.application.Platform;

//This is probably the buggiest part of Client right now.
public class ViewGui extends View {
    private final String startingScene;

    public ViewGui(String startingScene) {
        super();
        this.startingScene = startingScene;
    }

    @Override
    public void startGameView(Runnable finishSetup) {
        Platform.runLater(()-> {

            GuiController controller = SceneManager.getCurrentController();

            if (controller instanceof SubMenuController) {
                // If the GUI is NOT in the "Waiting for players" view and has received a START message,
                // it means it was in a lobby and the only reason it's not in the correct view is that
                // the GUI was the last player needed for the game to start
                ((SubMenuController) controller).successfullyJoined(); // Switch temporarily to "waiting for players"
                controller = SceneManager.getCurrentController();
                ((MenuWaitGameController) controller).startGame(); // And the start the game
                finishSetup.run();

            } else if (controller instanceof MenuWaitGameController) { // Normal startup
                ((MenuWaitGameController) controller).startGame();
                finishSetup.run();

            } else {
                throw new RuntimeException("Wrong GUI state");
            }

        });
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
        Platform.runLater(()->controller.showConnectionErrorDialog(message));
    }

    //Not having enough differentiation between success and error messages,
    //I had to figure out something. I know it's not very pretty using strings literals, but it was the fastest way.

    @Override
    public void handleSuccessMessage(String message) {
        GuiController controller = SceneManager.getCurrentController();
        switch (message) {
            case "NAME" -> { // When the name is correctly set
                if (controller instanceof SubMenuController)
                    Platform.runLater(() -> ((SubMenuController) controller).confirmUsername());
                else
                    throw new RuntimeException("Wrong GUI state");
            }
            case "Joined game" -> { // When the client has correctly entered a lobby
                if (controller instanceof SubMenuController)
                    Platform.runLater(() -> ((SubMenuController) controller).successfullyJoined());
                else
                    throw new RuntimeException("Wrong GUI state");
            }
            case "Successfully loaded game" -> { // When a game has been loaded
                if (controller instanceof MenuLoadGameController)
                    Platform.runLater(() -> ((MenuLoadGameController) controller).loadGame());
                else
                    throw new RuntimeException("Wrong GUI state");
            }
            case "PICK" -> { // When the tiles can be picked
                if (controller instanceof BoardController)
                    Platform.runLater(()->((BoardController)controller).notifyValidMove());
                else
                    throw new RuntimeException("Wrong GUI state");
            }

        }
    }

    @Override
    public void handleErrorMessage(String message) {
        GuiController controller = SceneManager.getCurrentController();
        if (message.equals("Another user has chosen this name")) {
            if (controller instanceof SubMenuController) {
                Platform.runLater(() -> ((SubMenuController) controller).notifyNameAlreadyTaken());
            }else {
                throw new RuntimeException("Wrong GUI state");
            }
        } else if (message.contains("whitelist")) { // This is the only reason I couldn't use a switch statement (thx Java)
            if (controller instanceof SubMenuController) {
                Platform.runLater(() -> ((SubMenuController) controller).notifyNotWhitelisted());
            }else {
                throw new RuntimeException("Wrong GUI state");
            }
        } else if (message.equals("Invalid tile range")) {
            if (controller instanceof BoardController) {
                Platform.runLater(() -> ((BoardController)controller).notifyInvalidMove());
            }else {
                throw new RuntimeException("Wrong GUI state");
            }
        } else{
            Platform.runLater(() -> controller.showErrorMessageDialog(message));
        }
    }

    @Override
    public void updateGameStatus(boolean isGameOver) {
    }

    @Override
    public void handleWinnerSelected(String winner) {
        GuiController controller = SceneManager.getCurrentController();
        if (controller instanceof BoardController)
            Platform.runLater(()->((BoardController)controller).endGame(winner));
        else
            throw new RuntimeException("Wrong GUI state");
    }

    @Override
    public void updatePlayerScore(String player, int score) {
        GuiController controller = SceneManager.getCurrentController();
        if (controller instanceof BoardController)
            Platform.runLater(()->((BoardController)controller).updatePlayerScore(player, score));
        else
            throw new RuntimeException("Wrong GUI state");
    }

    @Override
    public void blockUsersGameCommands() {

        super.blockUsersGameCommands();
        GuiController controller = SceneManager.getCurrentController();
        if (controller instanceof BoardController)
            Platform.runLater(()->((BoardController)controller).blockCommands());
        else
            throw new RuntimeException("Wrong GUI state");
    }

    @Override
    public void allowUsersGameCommands() {
        super.allowUsersGameCommands();
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
    public void finalizeUpdate() {

    }

    @Override
    public void updatePlayerConnectionStatus(String player, boolean isDisconnected) {
        GuiController controller = SceneManager.getCurrentController();
        if (controller instanceof BoardController)
            //TODO test this
            Platform.runLater(()->((BoardController)controller).updatePlayerConnection(player, isDisconnected));
        else
            throw new RuntimeException("Wrong GUI state");
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
        GuiController controller = SceneManager.getCurrentController();
        if (controller instanceof BoardController)
            Platform.runLater(()->((BoardController)controller).setCurrentTurn(turn));
        else
            throw new RuntimeException("Wrong GUI state");
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
}
