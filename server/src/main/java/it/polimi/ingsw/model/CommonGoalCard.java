package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.GoalCard;

import java.util.ArrayList;

public abstract class CommonGoalCard implements GoalCard {
    private final int id;
    private final ArrayList<Integer> points = new ArrayList<>();

    public CommonGoalCard(int id, int numPlayer) {
        this.id = id;
        points.add(4);
        points.add(8);
        if(numPlayer==4)
        {
            points.add(0,2);
            points.add(2,6);
        }
        else if(numPlayer==3)
        {
            points.add(1,6);
        }
    }

    public int getId() {
        return id;
    }

    public ArrayList<Integer> getPoints() {
        return new ArrayList<>(points);
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