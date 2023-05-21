package it.polimi.ingsw.util.observer;

import it.polimi.ingsw.server.messages.Message;

public interface ViewListener extends Listener {
    void update(Message message);
    void onChooseUsername(String username);
    void onChooseColumnOfBookshelf(int numberOfColumn);
    void onChooseTilesOrder(int first, int second, int third);
    void onChooseTilesOnBoard(int row1, int col1, int row2, int col2);
    void onChatMessageSent(String message);
    void onChatWhisperSent(String message, String recipient);
    void onQuitGame();
    void onSaveCurrentGame();
    void onLoadSavedGame(String saveName);
    void onListSavedGames();
    void onNewGame(int lobbySize);
    void onJoinGame();
    void onEndOfTurn();
    void onGenericInput(String rawInput);
}
