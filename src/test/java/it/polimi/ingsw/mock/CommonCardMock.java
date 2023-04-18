package it.polimi.ingsw.mock;

import it.polimi.ingsw.server.model.goals.common.CommonGoalCard;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.util.serialization.Serializer;

import java.util.ArrayList;
import java.util.List;

public class CommonCardMock extends CommonGoalCard {
    private final List<String> awardedPlayers;

    public CommonCardMock(int id, int numPlayer, List<String> awardedPlayers) {
        super(id, numPlayer);
        this.awardedPlayers = new ArrayList<>(awardedPlayers);
    }

    @Override
    public boolean canObtainPoints(IBookshelf bookshelf) {
        return true;
    }

    @Override
    public List<String> getAwardedPlayers() {
        return awardedPlayers;
    }

    @Override
    public ArrayList<Integer> getPoints() {
        ArrayList<Integer> points = super.getPoints();
        points = new ArrayList<>(points.subList(0, points.size() - awardedPlayers.size()));
        return points;
    }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.serialize(this);
    }
}
