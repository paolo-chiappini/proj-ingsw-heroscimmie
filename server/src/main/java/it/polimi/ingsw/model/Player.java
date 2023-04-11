package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.IBookshelf;
import it.polimi.ingsw.model.interfaces.IPlayer;

public class Player implements IPlayer {
    private final String username;
    private boolean firstPlayer;
    private IBookshelf bookshelf;
    private PersonalGoalCard personalGoalCard;
    private int score = 0;


    public Player(String username){
        this.username = username;
        this.firstPlayer=false;
    }

    /**
     * @return player username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return player score
     */
    public int getScore() {
        return score;
    }

    /**
     * @return player bookshelf
     */
    @Override
    public IBookshelf getBookshelf() {
        return bookshelf;
    }

    @Override
    public void setBookshelf(IBookshelf bookshelf) {
        this.bookshelf = bookshelf;
    }

    /**
     * Add points to the total amount if player scores
     * Points are awarded by adjacency among the cards on the bookshelf
     * personal goals and scoring tokens on the common goal cards
     * @param points ar the points to add to the score
     */
    public void addPointsToScore(int points){
        score += points;
    }

    /**
     * @param personalGoalCard is the personal goal card drowned by the player
     */
    public void setPersonalGoalCard(PersonalGoalCard personalGoalCard) {
        this.personalGoalCard = personalGoalCard;
    }

    /**
     * @return the player personal gaol card
     */
    public PersonalGoalCard getPersonalGoalCard() {
        return personalGoalCard;
    }

    /**
     * @param firstPlayer is true if the player is the first in Game
     */
    public void setFirstPlayer(boolean firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    /**
     * @return true if the player is the first of the game
     */
    public boolean isFirstPlayer() {
        return firstPlayer;
    }
}
