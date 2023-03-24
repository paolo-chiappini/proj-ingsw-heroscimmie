package it.polimi.ingsw.model;

public class Player {
    private String username;
    private int score; //depends on how many book in the shelf
    private PersonalGoalCard card;
    private Bookshelf bookshelf;

    public Player(String username, int score){
        this.score = score;
        this.username = username;
    }

    /**
     *
     * @return player username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return player score
     */
    public int getScore() {
        return score;
    }

    /**
     * Add points to the total amount if player scores
     * Points are awarded by adjacency among the cards on the bookshelf
     * personal goals and scoring tokens on the common goal cards
     * @param points
     */
    public void addPointsToScore(int points){
        //adjacency
        //personal goal
        points += card.evaluatePoints(bookshelf);
        //common goal

    }

    public void setCard(PersonalGoalCard card) {
        this.card = card;
    }

    public PersonalGoalCard getCard() {
        return card;
    }
}
