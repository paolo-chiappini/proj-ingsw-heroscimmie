package it.polimi.ingsw.client.virtualModel;

import it.polimi.ingsw.util.observer.ModelListener;
import it.polimi.ingsw.util.observer.ObservableObject;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class represents a turn in the virtual model.
 * It aims to represent the state of a turn in the client and update it if necessary
 */
public class ClientTurnState extends ObservableObject<ModelListener> {
    int currentTurn;
    String currentPlayer;
    boolean gameStatus;

    public ClientTurnState(){
        this.currentTurn = 0;
        this.gameStatus = false;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
        notifyListeners(listener -> listener.setCurrentTurn(currentTurn));
    }

    public void setGameStatus(boolean gameStatus) {
        this.gameStatus = gameStatus;
        notifyListeners(listener -> listener.updateGameStatus(gameStatus));
    }

    public void setCurrentPlayer(String player) {
        this.currentPlayer = player;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public boolean isGameOver() {
        return gameStatus;
    }

    public String getCurrentPlayer() { return currentPlayer; }

    /**
     * Updates the state of a turn
     * @param data contains up-to-date board details
     */
    public void updateTurnState(String data) {
        JSONObject json = new JSONObject(data);
        JSONArray playersOrder = json.getJSONArray("players_order");
        int currTurn = json.getInt("players_turn"); 
        boolean isLastLap = json.getBoolean("is_end_game");

        setGameStatus((isLastLap && currTurn == 0) || json.has("winner"));
        setCurrentTurn(currTurn);
        setCurrentPlayer(playersOrder.getString(currTurn));
    }
}
