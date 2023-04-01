package it.polimi.ingsw.model;

public class Player {
    private String username;
    private int score; //depends on how many book in the shelf
    private PersonalGoalCard personalGoalCard;
    private AdjacencyBonusGoal bonusGoal;
    private CommonGoalCard commonGoal;
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
        points+= bonusGoal.evaluatePoints(bookshelf);
        //personal goal
        points += personalGoalCard.evaluatePoints(bookshelf);
        //common goal
        points += commonGoal.evaluatePoints();
    }

    public void setPersonalGoalCard(PersonalGoalCard personalGoalCard) {
        this.personalGoalCard = personalGoalCard;
    }

    public PersonalGoalCard getPersonalGoalCard() {
        return personalGoalCard;
    }
}
