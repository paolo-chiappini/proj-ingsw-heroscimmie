package it.polimi.ingsw.client.view;

import it.polimi.ingsw.util.observer.ModelListener;
import it.polimi.ingsw.util.observer.ObservableObject;
import it.polimi.ingsw.util.observer.ViewListener;

public abstract class View
        extends ObservableObject<ViewListener>
        implements ModelListener, Runnable {

    public abstract void reset();
    public abstract void finalizeUpdate();

    public void notifyNameChange(String name) {
        notifyListeners(listener -> listener.onChooseUsername(name));
    }

    public void notifyNewGameCommand(int lobbySize) {
        notifyListeners(listener -> listener.onNewGame(lobbySize));
    }

    public void notifyJoinGameCommand() {
        notifyListeners(ViewListener::onJoinGame);
    }

    public void notifyQuitGameCommand() {
        notifyListeners(ViewListener::onQuitGame);
    }

    public void notifyListCommand() {
        notifyListeners(ViewListener::onListSavedGames);
    }

    public void notifyLoadCommand(int chosenGameIndex) {
        // TODO : change from string to index
        notifyListeners(listener -> listener.onLoadSavedGame(""));
    }

    public void notifySaveCommand() {
        notifyListeners(ViewListener::onSaveCurrentGame);
    }

    public void notifyNewChatMessage(String message) {
        notifyListeners(listener -> listener.onChatMessageSent(message));
    }

    public void notifyNewChatWhisper(String message, String recipient) {
        notifyListeners(listener -> listener.onChatWhisperSent(message, recipient));
    }

    public void notifyPickCommand(int row1, int col1, int row2, int col2) {
        notifyListeners(listener -> listener.onChooseTilesOnBoard(row1, col1, row2, col2));
    }

    public void notifyOrderCommand(int first, int second, int third) {
        notifyListeners(listener -> listener.onChooseTilesOrder(first, second, third));
    }

    public void notifyDropCommand(int column) {
        notifyListeners(listener -> listener.onChooseColumnOfBookshelf(column));
    }
}
