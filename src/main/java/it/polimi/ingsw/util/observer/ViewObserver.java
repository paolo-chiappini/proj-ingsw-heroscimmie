package it.polimi.ingsw.util.observer;

public interface ViewObserver {
    void updateGameStatus(boolean isGameOver);
    void updatePlayerScore(String player, int score);
    void updateBookshelf(String player, int[][] update);
    void updateBoard(int[][] update);
    void updatePlayerConnectionStatus(String player, boolean isDisconnected);
    void updateCommonGoalPoints(int cardId, int points);
    void setCommonGoal(int id, int points);
    void setPersonalGoal(int id);
    void setCurrentTurn(int turn);
    void addMessage(String message, String sender, boolean isWhisper);
    void addPlayer(String username, int score, boolean isClient);

}