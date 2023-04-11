package it.polimi.ingsw.util.serialization;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.interfaces.*;
import it.polimi.ingsw.model.interfaces.builders.ITurnManagerBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Represents a deserializer using the JSON format.
 */
public class JsonDeserializer implements Deserializer {

    @Override
    public void deserializeGame(String data) {

    }

    @Override
    public IPlayer deserializePlayer(String data) {
        return null;
    }

    @Override
    public IBoard deserializeBoard(String data) {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray boardArray = jsonObject.getJSONArray("board");

        for (int i = 0; i < boardArray.length(); i++) {
            JSONArray row = boardArray.getJSONArray(i);
            for (int j = 0; j < row.length(); j++) {
                // deserialize and set tile on board
                int tileOrdinal = row.getInt(j);
                if (tileOrdinal < 0) {
                    /*board.setTileAt(i, j, null);*/
                    continue;
                }

                TileType type = TileType.values()[tileOrdinal];
                /*board.setTileAt(i, j, new Tile(type));*/ // TO FIX: Bag.getTileByType(type);
            }
        }
        return null;
    }

    @Override
    public IBookshelf deserializeBookshelf(String data) {
        return null;
    }

    @Override
    public CommonGoalCard deserializeCommonGoalCard(String data) {
        return null;
    }

    @Override
    public IBag deserializeBag(String data) {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray tiles = jsonObject.getJSONArray("bag");

        for (int i = 0; i < TileType.values().length; i++) {
            /*bag.setRemainingTilesCount(TileType.values()[i], tiles.getInt(i));*/
        }
        return null;
    }

    @Override
    public ITurnManager deserializeTurn(String data) {
        JSONObject jsonObject = new JSONObject(data);
        ITurnManagerBuilder turnBuilder = new TurnManager.TurnManagerBuilder();

        JSONArray usernames = jsonObject.getJSONArray("players_order");
        for (int i = 0; i < usernames.length(); i++) {
            turnBuilder.addPlayer(new Player(usernames.getString(i)));
        }

        turnBuilder.setCurrentTurn(jsonObject.getInt("players_turn"));
        turnBuilder.setIsEndGame(jsonObject.getBoolean("is_end_game"));
        return turnBuilder.build();
    }
}
