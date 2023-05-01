package it.polimi.ingsw.client.virtualModel;

import it.polimi.ingsw.util.Observable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the game in the virtual model.
 * It aims to represent information of a game in the client and update them if necessary
 */
public class ClientGame extends Observable {
    private ClientTurnManager turnManager;
    private ClientBoard board;
    private List<ClientCommonGoalCard> commonGoals;
    private List<ClientPlayer> players;
    private ClientPlayer winner;

    public ClientGame(int numberOfPlayers) {
        this.players=new ArrayList<>();
        for(int i=0; i<numberOfPlayers;i++)
            players.add(new ClientPlayer("anonymous"));
        this.turnManager=new ClientTurnManager(players);
        this.commonGoals = new ArrayList<>();
        commonGoals.add(new ClientCommonGoalCard(numberOfPlayers));
        commonGoals.add(new ClientCommonGoalCard(numberOfPlayers));
        this.winner = null;
        this.board = new ClientBoard(numberOfPlayers);
    }

    public void setBoard(ClientBoard board) {
        this.board = board;
    }

    public void setCommonGoalCards(List<ClientCommonGoalCard> commonGoals) {
        this.commonGoals = new ArrayList<>(commonGoals);
    }
    public void setPlayers(List<ClientPlayer> players) {
        this.players = new ArrayList<>(players);
    }
    public void setTurnManager(ClientTurnManager turnManager) {
        this.turnManager = turnManager;
    }

    public void setWinner(ClientPlayer winner) {
        this.winner = winner;
    }

    public ClientBoard getBoard() { return board; }

    public List<ClientCommonGoalCard> getCommonGoals() {
        return new ArrayList<>(commonGoals);
    }

    public List<ClientPlayer> getPlayers() {
        return new ArrayList<>(turnManager.getPlayersInOrder());
    }

    public ClientTurnManager getTurnManager() {
        return turnManager;
    }

    public String getWinner() {
        return winner.getUsername();
    }

    /**
     * Updates all the information of the current player who is playing the turn
     * @param data contains up-to-date players details
     */
    public void updatePlayer(String data)
    {
        turnManager.getPlayersInOrder().get(turnManager.getCurrentPlayerIndex()).updatePlayer(data);
    }

    /**
     * Updates all the information of the game
     * @param data contains up-to-date game details
     */
    public void updateGame(String data)
    {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray jsonPlayersArray, jsonCommonGoalsArray;
        this.board.updateBoard(data);
        jsonCommonGoalsArray = jsonObject.getJSONArray("common_goals");
        for(int i=0;i<commonGoals.size();i++) {
            commonGoals.get(i).updateId(jsonCommonGoalsArray.getString(i));
            commonGoals.get(i).updateAwardedPlayers(jsonCommonGoalsArray.getString(i));
            commonGoals.get(i).updatePoints(jsonCommonGoalsArray.getString(i));
        }
        this.turnManager.updateGameIsOver(data);
        jsonPlayersArray = jsonObject.getJSONArray("players");
        updatePlayer(jsonPlayersArray.getString(turnManager.getCurrentPlayerIndex()));
        this.turnManager.updatePlayersOrder(data);
        this.turnManager.updatePlayerInTurn(data);
    }
}