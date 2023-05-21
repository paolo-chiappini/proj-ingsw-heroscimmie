package it.polimi.ingsw.client.virtualModel;

import it.polimi.ingsw.util.observer.ModelListener;
import it.polimi.ingsw.util.observer.ObservableObject;
import org.json.JSONObject;

/**
 * This class represents a single player in the virtual model.
 * It aims to represent the state of a player in the client and update it if necessary
 */
public class ClientPlayer extends ObservableObject<ModelListener> {
    private String username;
    private ClientBookshelf bookshelf;
    private int idPersonalGoalCard;
    private int score;
    private boolean isDisconnected;

    public ClientPlayer(String username){
        this.username = username;
        this.bookshelf = new ClientBookshelf();
        this.idPersonalGoalCard = 0;
        this.score = 0;
        this.isDisconnected = false;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setBookshelf(ClientBookshelf bookshelf) {
        this.bookshelf = bookshelf;
        notifyListeners(listener->listener.updateBookshelf(getUsername(), getBookshelf().getTiles()));
    }

    public void setIdPersonalGoalCard(int idPersonalGoalCard) {
        this.idPersonalGoalCard = idPersonalGoalCard;
        notifyListeners(listener->listener.setPersonalGoal(idPersonalGoalCard));
    }

    public void setScore(int score) {
        this.score = score;
        notifyListeners(listener->listener.updatePlayerScore(getUsername(),getScore()));
    }

    public void setDisconnected(boolean disconnected) {
        isDisconnected = disconnected;
        notifyListeners(listener->listener.updatePlayerConnectionStatus(username,disconnected));
    }

    public ClientBookshelf getBookshelf() {
        return bookshelf;
    }

    public int getPersonalCardId() {
        return idPersonalGoalCard;
    }

    public int getScore() {
        return score;
    }

    public String getUsername() {
        return username;
    }

    public boolean isDisconnected() {
        return isDisconnected;
    }

    /**
     * Updates all the information of a player (bookshelf and score)
     * @param data contains up-to-date player details
     */
    public void updatePlayer(String data){
        this.getBookshelf().updateBookshelf(data);
        this.updateScore(data);
        notifyListeners(listener->listener.updateBookshelf(getUsername(), getBookshelf().getTiles()));
    }

    /**
     * Updates the score of the player
     * @param data contains up-to-date player details
     */
    public void updateScore(String data) {
        JSONObject jsonObject = new JSONObject(data);
        int scorePlayer = jsonObject.getInt("score");
        setScore(scorePlayer);
    }

    /**
     * Updates the card's ID
     * @param data contains up-to-date card details
     */
    public void updatePersonalCardId(String data)
    {
        JSONObject jsonObject = new JSONObject(data);
        int idCard = jsonObject.getInt("personal_card_id");
        setIdPersonalGoalCard(idCard);
    }

    /**
     * Updates the player's username
     * @param data contains up-to-date card details
     */
    public void updateUsername(String data)
    {
        JSONObject jsonObject = new JSONObject(data);
        String name = jsonObject.getString("username");
        setUsername(name);
    }
}