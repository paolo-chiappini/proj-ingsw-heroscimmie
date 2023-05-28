package it.polimi.ingsw.mock;

import it.polimi.ingsw.server.model.player.IPlayer;
import it.polimi.ingsw.server.model.turn.ITurnManager;
import it.polimi.ingsw.util.serialization.Serializer;

import java.util.ArrayList;
import java.util.List;

public class TurnManagerMock implements ITurnManager {
    final List<IPlayer> players;
    int turn;
    boolean lastLap;

    public TurnManagerMock(List<IPlayer> players, int turn, boolean lastLap) {
        this.players = new ArrayList<>(players);
        this.turn = turn;
        this.lastLap = lastLap;
    }

    @Override
    public boolean isLastLap() {
        if (players.get(turn).getBookshelf().full()) lastLap = true;
        return lastLap;
    }

    @Override
    public boolean isGameOver() {
        return turn == 0 && isLastLap();
    }

    @Override
    public void nextTurn() {
        turn++;
        turn = turn % players.size();
    }

    @Override
    public IPlayer getCurrentPlayer() {
        return players.get(turn);
    }

    @Override
    public List<IPlayer> getPlayersInOrder() {
        return new ArrayList<>(players);
    }

    @Override
    public String serialize(Serializer serializer) {
        return serializer.serialize(this);
    }
}
