package it.polimi.ingsw.server.model.goals.common;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.player.IPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonGoalCard implements GoalCard {
    private final int id;
    private List<Integer> points = new ArrayList<>();
    private List<String> players;

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

    public List<String> getAwardedPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Builder used during the deserialization of a common goal card.
     */
    public static class CommonGoalCardBuilder implements ICommonGoalCardBuilder {
        private final CommonGoalCard instance;

        /**
         * @param id card id.
         */
        public CommonGoalCardBuilder(int id) {
            instance = new CommonGoalCardDeck(0).getCommonGoalCards().get(id - 1);
            instance.points = new ArrayList<>();
            instance.players = new ArrayList<>();
        }

        @Override
        public CommonGoalCard build() {
            return instance;
        }

        @Override
        public ICommonGoalCardBuilder addPoints(int points) {
            instance.points.add(points);
            return this;
        }

        @Override
        public ICommonGoalCardBuilder addPlayer(String playerName) {
            instance.players.add(playerName);
            return this;
        }
    }
}