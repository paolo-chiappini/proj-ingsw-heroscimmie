package it.polimi.ingsw.util.observer;

import it.polimi.ingsw.server.messages.Message;

public interface ControllerObserver {
    void update(Message message);
    void onChooseUsername(String username);
    void onChoosePlayersNumber(int playersNumber);
   void onChooseColumnOfBookshelf(int numberOfColumn);
    void onChooseTilesOrder(int first, int second, int third);
    void onChooseTilesOnBoard(int row1, int col1, int row2, int col2);
}
