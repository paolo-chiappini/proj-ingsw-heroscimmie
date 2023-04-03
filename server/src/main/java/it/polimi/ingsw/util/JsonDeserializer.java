package it.polimi.ingsw.util;

import it.polimi.ingsw.model.CommonGoalCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.interfaces.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonDeserializer implements Deserializer {

    @Override
    public void deserializeGame(String data) {

    }

    @Override
    public void deserializePlayer(IPlayer player, String data) {

    }

    @Override
    public void deserializeBoard(String data) {

    }

    @Override
    public void deserializeBookshelf(IBookshelf bookshelf, String data) {

    }

    @Override
    public void deserializePersonalGoalCard(String data) {

    }

    @Override
    public void deserializeCommonGoalCard(CommonGoalCard commonGoalCard, String data) {

    }

    @Override
    public void deserializeBag(String data) {

    }

    @Override
    public void deserializeBoardSpace(BoardSpace space, String data) {

    }

    @Override
    public void deserializeTurn(ITurnManager turnManager, String data) {
        JSONObject jsonObject = new JSONObject(data);
        List<IPlayer> players = new ArrayList<>();

        JSONArray usernames = jsonObject.getJSONArray("players_order");
        for (int i = 0; i < usernames.length(); i++) {
            players.add(new Player(usernames.getString(i), 0));
        }

        turnManager.setPlayersOrder(players);
        turnManager.setCurrentTurn(jsonObject.getInt("players_turn"));
    }
}
