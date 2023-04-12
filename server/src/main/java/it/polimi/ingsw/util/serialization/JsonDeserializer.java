package it.polimi.ingsw.util.serialization;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.interfaces.*;
import it.polimi.ingsw.model.interfaces.builders.IBagBuilder;
import it.polimi.ingsw.model.interfaces.builders.ITurnManagerBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a deserializer using the JSON format.
 */
public class JsonDeserializer implements Deserializer {

    @Override
    public Game deserializeGame(String data) {
        JSONObject jsonObject = new JSONObject(data);
        JSONObject jsonGameState;
        JSONArray jsonPlayersArray, jsonCommonGoalsArray;
        IBag bag;
        IBoard board;
        ITurnManager turnManager;
        ITurnManagerBuilder turnManagerBuilder = new TurnManager.TurnManagerBuilder();
        List<CommonGoalCard> commonGoalCards = new ArrayList<>();

        jsonGameState = jsonObject.getJSONObject("game");

        jsonPlayersArray = jsonObject.getJSONArray("players");
        jsonCommonGoalsArray = jsonGameState.getJSONArray("common_goals");

        bag = deserializeBag(jsonObject.getJSONArray("bag").toString());
        board = deserializeBoard(data);
        turnManager = deserializeTurn(jsonGameState.toString());

        jsonPlayersArray.forEach(p -> turnManagerBuilder.addPlayer(deserializePlayer(p.toString())));

        return new Game.GameBuilder()
                .setCommonGoalCards(commonGoalCards)
                .setTilesBag(bag)
                .setBoard(board)
                .setTurnManager(turnManager)
                .build();
    }

    @Override
    public IPlayer deserializePlayer(String data) {
        JSONObject jsonObject = new JSONObject(data);
        String username = jsonObject.getString("username");
        int score = jsonObject.getInt("score");
        int personalCardId = jsonObject.getInt("personal_card_id");
        IBookshelf bookshelf = deserializeBookshelf(jsonObject.getJSONArray("bookshelf").toString());

        IPlayer player = new Player(username);
        player.setBookshelf(bookshelf);
        player.setPersonalGoalCard(new PersonalGoalCardDeck().getCardById(personalCardId));
        player.addPointsToScore(score);

        return player;
    }

    @Override
    public IBoard deserializeBoard(String data) {
        JSONArray boardArray = new JSONArray(data);
        int[][] boardTiles = deserializeTileGrid(boardArray);

        return null;
    }

    @Override
    public IBookshelf deserializeBookshelf(String data) {
        JSONArray bookshelfArray = new JSONArray(data);
        int [][] bookshelfTiles = deserializeTileGrid(bookshelfArray);

        return null;
    }

    private int[][] deserializeTileGrid(JSONArray jsonArray) {
        int[][] grid = new int[jsonArray.length()][jsonArray.getJSONArray(0).length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONArray row = jsonArray.getJSONArray(i);
            for (int j = 0; j < row.length(); j++) {
                int tileOrdinal = row.getInt(j);
                grid[i][j] = tileOrdinal;
            }
        }

        return grid;
    }

    @Override
    public CommonGoalCard deserializeCommonGoalCard(String data) {
        return null;
    }

    @Override
    public IBag deserializeBag(String data) {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray tiles = jsonObject.getJSONArray("bag");
        IBagBuilder builder = new Bag.BagBuilder();

        for (int i = 0; i < tiles.length(); i++) {
            builder.setRemainingTilesCount(TileType.values()[i], tiles.getInt(i));
        }

        return builder.build();
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
