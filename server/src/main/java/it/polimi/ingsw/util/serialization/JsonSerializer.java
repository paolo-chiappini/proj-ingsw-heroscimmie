package it.polimi.ingsw.util.serialization;

import it.polimi.ingsw.model.CommonGoalCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.interfaces.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a serializer using the JSON format.
 */
public class JsonSerializer implements Serializer {
    @Override
    public String serialize(Game game) {
        List<IPlayer> players = game.getPlayers();
        List<CommonGoalCard> commonGoalCards = game.getCommonGoals();
        ITurnManager turnManager = game.getTurnManager();
        IBag bag = game.getBag();
        IBoard board = game.getBoard();

        JSONArray jsonPlayersArray = new JSONArray();
        players.forEach(p -> jsonPlayersArray.put(new JSONObject(serialize(p))));

        JSONArray jsonCommonGoalsArray = new JSONArray();
        commonGoalCards.forEach(c -> jsonCommonGoalsArray.put(new JSONObject(serialize(c))));

        JSONArray jsonBagArray = new JSONArray(serialize(bag));
        JSONArray jsonBoardArray = new JSONArray(serialize(board));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("board", jsonBoardArray);
        jsonObject.put("bag", jsonBagArray);
        jsonObject.put("players", jsonPlayersArray);
        jsonObject.put("common_goals", jsonCommonGoalsArray);

        jsonObject = mergeJsonObjects(List.of(
                new JSONObject(serialize(turnManager)),
                jsonObject
        ));

        return jsonObject.toString();
    }

    @Override
    public String serialize(IPlayer player) {
        IBookshelf bookshelf = player.getBookshelf();
        JSONObject jsonObject = new JSONObject();

        String serializedBookshelf = serialize(bookshelf);
        JSONArray jsonBookshelf = new JSONArray(serializedBookshelf);

        jsonObject.put("username", player.getUsername());
        jsonObject.put("score", player.getScore());
        jsonObject.put("bookshelf", jsonBookshelf);
        jsonObject.put("personal_card_id", player.getPersonalGoalCard().getId());

        return jsonObject.toString();
    }

    @Override
    public String serialize(IBoard board) {
        JSONArray rows = new JSONArray();

        for (int i = 0; i < board.getSize(); i++) {
            JSONArray row = new JSONArray();
            for (int j = 0; j < board.getSize(); j++) {
                GameTile tile = board.getTileAt(i, j);
                if (tile == null) {
                    row.put(-1);
                    continue;
                }

                row.put(tile.getType().ordinal());
            }
            rows.put(row);
        }

        return rows.toString();
    }

    @Override
    public String serialize(IBookshelf bookshelf) {
        JSONArray grid = new JSONArray();

        for (int row = 0; row < bookshelf.getHeight(); row++) {
            JSONArray rowArray = new JSONArray();
            for (int col = 0; col < bookshelf.getWidth(); col++) {
                int tileTypeInt = -1; // equivalent to null

                if (bookshelf.hasTile(row, col)) {
                    tileTypeInt = bookshelf.getTileAt(row, col).getType().ordinal();
                }

                rowArray.put(tileTypeInt);
            }
            grid.put(rowArray);
        }

        return grid.toString();
    }

    @Override
    public String serialize(CommonGoalCard commonGoalCard) {
        JSONObject jsonObject = new JSONObject();

        List<Integer> points = commonGoalCard.getPoints();
        JSONArray jsonPoints = new JSONArray(points);

        /*List<String> playerNames = */
        JSONArray jsonNamesArray = new JSONArray();

        jsonObject.put("card_id", commonGoalCard.getId());
        jsonObject.put("points", jsonPoints);
        jsonObject.put("valid_players", jsonNamesArray);

        return jsonObject.toString();
    }

    @Override
    public String serialize(IBag bag) {
        JSONArray jsonArray = new JSONArray();
        HashMap<TileType, Integer> tilesCounts = bag.getTilesBag();

        tilesCounts.forEach((key, value) -> {
            jsonArray.put(value);
        });

        return jsonArray.toString();
    }

    @Override
    public String serialize(ITurnManager turnManager) {
        List<IPlayer> players = turnManager.getPlayersOrder();
        JSONObject jsonObject = new JSONObject();
        JSONArray usernames = new JSONArray();

        players.forEach(p -> usernames.put(p.getUsername()));
        int currentTurn = players.indexOf(turnManager.getCurrentPlayer());

        jsonObject.put("players_turn", currentTurn);
        jsonObject.put("is_end_game", turnManager.isLastLap());
        jsonObject.put("players_order", usernames);

        return jsonObject.toString();
    }

    public JSONObject mergeJsonObjects(List<JSONObject> serializedObjects) {
        JSONObject merged = new JSONObject();

        for (JSONObject obj : serializedObjects) {
            for (Iterator<String> it = obj.keys(); it.hasNext(); ) {
                String key = it.next();
                merged.put(key, obj.get(key));
            }
        }

        return merged;
    }
}
