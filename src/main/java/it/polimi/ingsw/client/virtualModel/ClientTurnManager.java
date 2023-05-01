package it.polimi.ingsw.client.virtualModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the turn manager in the virtual model.
 * It aims to represent information of a game turn in the client and update them if necessary
 */
public class ClientTurnManager {
    private int currentPlayerIndex;
    private boolean lastLap;
    private List<ClientPlayer> players;

    public ClientTurnManager(List<ClientPlayer> players) {
        this.players = new ArrayList<>(players);
        this.lastLap = false;
        currentPlayerIndex = 0;
    }

    public void setLastLap(boolean lastLap) {
        this.lastLap = lastLap;
    }

    public void setEndGame(boolean endGame)
    {
        if(endGame){
            lastLap=true;
            currentPlayerIndex=0;
        }
    }

    /**
     * Set the list of players in the game
     * @param players list of players playing the game
     */
    public void setPlayers(List<ClientPlayer> players)
    {
        this.players=players;
    }

    /**
     * Set the index of the current player in turn playing the game
     * @param currentPlayerIndex is the index of the current player
     */
    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public boolean isLastLap() {
        return lastLap;
    }

    public boolean isGameOver() {
        return  lastLap && (currentPlayerIndex == 0);
    }

    public List<ClientPlayer> getPlayersInOrder() {
        return new ArrayList<>(players);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public ClientPlayer getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Updates endgame information
     * @param data contains up-to-date card details
     */
    public void updateGameIsOver(String data) {
        JSONObject jsonObject = new JSONObject(data);
        boolean endGame= jsonObject.getBoolean("is_end_game");
        setEndGame(endGame);
    }

    /**
     * Updates the order of players in the game
     * @param data contains up-to-date card details
     */
    public void updatePlayersOrder(String data) {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray playersOrder = jsonObject.getJSONArray("players_order");
        List<ClientPlayer> players = new ArrayList<>();
        for (int i = 0; i < playersOrder.length(); i++) {
            players.add(new ClientPlayer(playersOrder.getString(i)));
        }
        setPlayers(players);
    }

    /**
     * Updates player's turn
     * @param data contains up-to-date card details
     */
    public void updatePlayerInTurn(String data) {
        JSONObject jsonObject = new JSONObject(data);
        int playerInTurn = jsonObject.getInt("players_turn");
        setCurrentPlayerIndex(playerInTurn);
    }
}