package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.goals.personal.PersonalGoalCard;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.util.serialization.Serializer;

public class Player implements IPlayer {
    private final String username;
    private IBookshelf bookshelf;
    private PersonalGoalCard personalGoalCard;
    private int score = 0;


    public Player(String username){
        this.username = username;
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
     * @param points are the points to add to the score
     */
    public void addPointsToScore(int points){
        score += points;
    }

    /**
     * @param personalGoalCard is the personal goal card drawn by the player
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

    @Override
    public String serialize(Serializer serializer) {
        return serializer.serialize(this);
    }
}
