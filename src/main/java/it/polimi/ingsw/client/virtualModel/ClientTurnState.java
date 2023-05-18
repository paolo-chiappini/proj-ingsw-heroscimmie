package it.polimi.ingsw.client.virtualModel;

import it.polimi.ingsw.util.observer.ModelObservable;

/**
 * This class represents a turn in the virtual model.
 * It aims to represent the state of a turn in the client and update it if necessary
 */
public class ClientTurnState extends ModelObservable {
    int currentTurn;
    boolean gameStatus;

    public ClientTurnState(){
        this.currentTurn = 0;
        this.gameStatus = false;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
        notifyObservers(obs -> obs.setCurrentTurn(currentTurn));
    }

    public void setGameStatus(boolean gameStatus) {
        this.gameStatus = gameStatus;
        notifyObservers(obs->obs.updateGameStatus(gameStatus));
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public boolean isGameStatus() {
        return gameStatus;
    }
}
