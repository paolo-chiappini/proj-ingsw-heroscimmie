package it.polimi.ingsw.util.observer;

import it.polimi.ingsw.server.messages.Message;

public interface ControllerObserver {
    void onMessageReceived(Message message);

    void onListReceived(Message message);

    void onChatMessageReceived(Message message);

    void update(Message message);

    void onGameStart(Message message);

    void joinGame();

    void newGame(int lobbySize);

    void listSavedGames();

    void loadSavedGame(String saveName);

    void saveCurrentGame();

    void quitGame();

    void onChooseUsername(String username);
    void onChoosePlayersNumber(int playersNumber);
    void onChooseColumnOfBookshelf(int numberOfColumn);
    void onChooseTilesOrder(int first, int second, int third);
    void onChooseTilesOnBoard(int row1, int col1, int row2, int col2);
    void onChatMessageSent(String message);
    void onChatWhisperSent(String message, String recipient);
}
