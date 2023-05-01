package it.polimi.ingsw.client.virtualModel;

import org.json.JSONObject;

/**
 * This class represents a single player in the virtual model.
 * It aims to represent the state of a player in the client and update it if necessary
 */
public class ClientPlayer {
    private String username;
    private ClientBookshelf bookshelf;
    private int idPersonalGoalCard;
    private int score;

    public ClientPlayer(String username){
        this.username = username;
        this.bookshelf = new ClientBookshelf();
        this.idPersonalGoalCard = 0;
        this.score = 0;
    }

    public void setUsername(String username){this.username = username;}
    public void setBookshelf(ClientBookshelf bookshelf) {
        this.bookshelf = bookshelf;
    }
    public void setIdPersonalGoalCard(int idPersonalGoalCard) {
        this.idPersonalGoalCard = idPersonalGoalCard;
    }
    public void setScore(int score) {
        this.score = score;
    }

    public ClientBookshelf getBookshelf() {
        return bookshelf;
    }

    public int getId() {
        return idPersonalGoalCard;
    }

    public int getScore() {
        return score;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Updates all the information of a player (bookshelf, personal goal card and score)
     * @param data contains up-to-date player details
     */
    public void updatePlayer(String data){
        this.updateUsername(data);
        this.getBookshelf().updateBookshelf(data);
        this.updateId(data);
        this.updateScore(data);
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
    public void updateId(String data)
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