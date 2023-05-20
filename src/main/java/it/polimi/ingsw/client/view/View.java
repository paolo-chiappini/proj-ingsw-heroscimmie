package it.polimi.ingsw.client.view;

import it.polimi.ingsw.util.observer.ModelListener;
import it.polimi.ingsw.util.observer.ObservableObject;
import it.polimi.ingsw.util.observer.ViewListener;

/**
 * Represents a generic view.
 * The view is run on a different thread and can be observed
 * by listeners while observing the model.
 */
public abstract class View
        extends ObservableObject<ViewListener>
        implements ModelListener, Runnable {

    /**
     * Method used to restore a "reset" state for the current view.
     */
    public abstract void reset();

    /**
     * Executes all the procedures needed to correctly show
     * the last update to the user.
     * Procedures may vary based on the view's necessities: ie graphics updates,
     * housekeeping and others.
     */
    public abstract void finalizeUpdate();

    /**
     * Shows the list of saved games to the user.
     * @param savedGames collection of saved games.
     */
    public abstract void showListOfSavedGames(String[] savedGames);

    /**
     * Shows a server connection error message to the user.
     */
    public abstract void showServerConnectionError();

    public abstract void handleSuccessMessage(String message);
    public abstract  void handleErrorMessage(String message);

    /**
     * Notifies all listeners that the player has changed username.
     * @param name player's username.
     */
    public void notifyNameChange(String name) {
        notifyListeners(listener -> listener.onChooseUsername(name));
    }

    /**
     * Notifies all listeners that the player has requested the creation of a new game.
     * @param lobbySize size of the lobby to create.
     */
    public void notifyNewGameCommand(int lobbySize) {
        notifyListeners(listener -> listener.onNewGame(lobbySize));
    }

    /**
     * Notifies all listeners that the player has requested to join a game.
     */
    public void notifyJoinGameCommand() {
        notifyListeners(ViewListener::onJoinGame);
    }

    /**
     * Notifies all listeners that the player has requested to quit the current game.
     */
    public void notifyQuitGameCommand() {
        notifyListeners(ViewListener::onQuitGame);
    }

    /**
     * Notifies all listeners that the player has requested to view the list of
     * all saved games.
     */
    public void notifyListCommand() {
        notifyListeners(ViewListener::onListSavedGames);
    }

    /**
     * Notifies all listeners that the player has requested to load a saved game.
     * @param chosenGameIndex index of the game to load.
     */
    public void notifyLoadCommand(int chosenGameIndex) {
        // TODO : change from string to index
        notifyListeners(listener -> listener.onLoadSavedGame(""));
    }

    /**
     * Notifies all listeners that the player has requested to save the current game.
     */
    public void notifySaveCommand() {
        notifyListeners(ViewListener::onSaveCurrentGame);
    }

    /**
     * Notifies all listeners that the player has sent a new chat message.
     * @param message contents of the message to send.
     */
    public void notifyNewChatMessage(String message) {
        notifyListeners(listener -> listener.onChatMessageSent(message));
    }

    /**
     * Notifies all listeners that the player has sent a new private message.
     * @param message contents of the message to send.
     * @param recipient recipient of the private message.
     */
    public void notifyNewChatWhisper(String message, String recipient) {
        notifyListeners(listener -> listener.onChatWhisperSent(message, recipient));
    }

    /**
     * Notifies all listeners that the player has requested to pick tiles from the board.
     * @param row1 row coordinate of the first tile in the range.
     * @param col1 column coordinate of the first tile in the range.
     * @param row2 row coordinate of the second tile in the range.
     * @param col2 column coordinate of the second tile in the range.
     */
    public void notifyPickCommand(int row1, int col1, int row2, int col2) {
        notifyListeners(listener -> listener.onChooseTilesOnBoard(row1, col1, row2, col2));
    }

    /**
     * Notifies all listeners that the player has requested to order the picked tiles.
     * @param first index of the first tile to drop.
     * @param second index of the second tile to drop.
     * @param third index of the third tile to drop.
     */
    public void notifyOrderCommand(int first, int second, int third) {
        notifyListeners(listener -> listener.onChooseTilesOrder(first, second, third));
    }

    /**
     * Notifies all listeners that the player has requested to drop tiles in the bookshelf.
     * @param column column of the bookshelf where to drop the tiles.
     */
    public void notifyDropCommand(int column) {
        notifyListeners(listener -> listener.onChooseColumnOfBookshelf(column));
    }
}
