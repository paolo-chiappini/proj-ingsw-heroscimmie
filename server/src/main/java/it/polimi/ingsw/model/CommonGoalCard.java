package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.GoalCard;

import java.util.ArrayList;

public abstract class CommonGoalCard implements GoalCard {
    private final int id;
    private ArrayList<Integer> points;

    public CommonGoalCard(int id, ArrayList<Integer> points) {
        this.id = id;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Integer> getPoints() {
        return points;
    }

    /**
     * evaluate the score of the cards
     * @return the score of the common goal cards
     **/
    @Override
    public int evaluatePoints() {
        return points.remove(points.size()-1);
    }
}