package it.polimi.ingsw.util;

import it.polimi.ingsw.model.CommonGoalCard;
import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.interfaces.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Represents a serializer using the JSON format.
 */
public class JsonSerializer implements Serializer {
    @Override
    public String serialize() {
        return null;
    }

    @Override
    public String serialize(IPlayer player) {
        IBookshelf bookshelf = player.getBookshelf();
        JSONObject jsonObject = new JSONObject();

        String serializedBookshelf = serialize(bookshelf);
        JSONObject jsonBookshelf = new JSONObject(serializedBookshelf);

        jsonObject.put("username", player.getUsername());
        jsonObject.put("score", player.getScore());
        jsonObject.put("bookshelf", jsonBookshelf);

        return jsonObject.toString();
    }

    @Override
    public String serialize(IBoard board) {
        JSONArray rows = new JSONArray(); 
        JSONObject jsonObject = new JSONObject();

        for (int i = 0; i < board.getSize(); i++) {
            JSONArray row = new JSONArray();
            for (int j = 0; j < board.getSize(); j++) {
                GameTile tile = board.getTileAt(i, j);
                if (tile == null) {
                    row.put(j, -1);
                    continue;
                }

                row.put(j, tile.getType().ordinal());
            }
            rows.put(i, row);
        }

        jsonObject.put("board", rows);
        return jsonObject.toString();
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

        jsonObject.put("card_id", commonGoalCard.getId());
        jsonObject.put("points", jsonPoints);
        jsonObject.put("", 0);

        return null;
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
}
