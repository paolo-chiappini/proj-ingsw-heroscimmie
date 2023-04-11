package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.interfaces.GoalCard;
import it.polimi.ingsw.model.interfaces.IPlayer;

import java.util.ArrayList;

public abstract class CommonGoalCard implements GoalCard {
    private final int id;
    private final ArrayList<Integer> points = new ArrayList<>();
    private final ArrayList<String> players;

    public CommonGoalCard(int id, int numPlayer) {
        this.id = id;
        players = new ArrayList<>();
        points.add(4);
        points.add(8);
        if(numPlayer==4)
        {
            points.add(0,2);
            points.add(2,6);
        }
        else if(numPlayer==3)
            points.add(1,6);
    }

    public int getId() {
        return id;
    }

    public ArrayList<Integer> getPoints() {
        return new ArrayList<>(points);
    }

    /**
     * evaluates the score of the cards
     * @return the score of the common goal cards
     **/
    @Override
    public int evaluatePoints(IPlayer player)
    {
        if(players.contains(player.getUsername()))
            throw new IllegalActionException("You can only score points from the same common goal card once per game");
        else
        {
            players.add(player.getUsername());
            return points.remove(points.size()-1);
        }
    }
}