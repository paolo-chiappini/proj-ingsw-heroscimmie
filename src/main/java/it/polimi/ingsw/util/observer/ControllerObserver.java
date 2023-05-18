package it.polimi.ingsw.util.observer;

import it.polimi.ingsw.server.messages.Message;

public interface ControllerObserver {
    void update(Message message);
    void onChooseUsername(String username);
    void onChooseColumnOfBookshelf(int numberOfColumn);
    void onChooseTilesOrder(int first, int second, int third);
    void onChooseTilesOnBoard(int row1, int col1, int row2, int col2);
    void onChatMessageSent(String message);
    void onChatWhisperSent(String message, String recipient);
    void quitGame();
    void saveCurrentGame();
    void loadSavedGame(String saveName);
    void listSavedGames();
    void newGame(int lobbySize);
    void joinGame();
    void onEndOfTurn();
}
