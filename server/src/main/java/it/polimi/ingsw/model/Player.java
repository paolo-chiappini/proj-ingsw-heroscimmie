package it.polimi.ingsw.model;

public class Player {
    private String username;
    private int score;
    private PersonalGoalCard personalGoalCard;

    /*
    private AdjacencyBonusGoal bonusGoal;
    private Bookshelf bookshelf;
    */

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
        /*
        points+= bonusGoal.evaluatePoints(bookshelf);   //adjacency
        points += personalGoalCard.evaluatePoints(bookshelf);   //personal goal
        */
        score += points;
    }

    public void setPersonalGoalCard(PersonalGoalCard personalGoalCard) {
        this.personalGoalCard = personalGoalCard;
    }

    public PersonalGoalCard getPersonalGoalCard() {
        return personalGoalCard;
    }
}
