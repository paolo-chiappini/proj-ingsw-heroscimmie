package it.polimi.ingsw.util.serialization;

import it.polimi.ingsw.server.model.goals.common.ICommonGoalCardBuilder;
import it.polimi.ingsw.server.model.turn.ITurnManagerBuilder;
import it.polimi.ingsw.server.model.bag.Bag;
import it.polimi.ingsw.server.model.bag.IBag;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.IBoard;
import it.polimi.ingsw.server.model.bookshelf.Bookshelf;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.goals.common.CommonGoalCard;
import it.polimi.ingsw.server.model.goals.personal.PersonalGoalCardDeck;
import it.polimi.ingsw.server.model.player.IPlayer;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.tile.TileType;
import it.polimi.ingsw.server.model.turn.ITurnManager;
import it.polimi.ingsw.server.model.turn.TurnManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a deserializer using the JSON format.
 */
public class JsonDeserializer implements Deserializer {

    @Override
    public Game deserializeGame(String data) {
        JSONObject jsonObject = new JSONObject(data);
        JSONArray jsonPlayersArray, jsonCommonGoalsArray;
        IBag bag;
        IBoard board;
        ITurnManager turnManager;
        List<CommonGoalCard> commonGoalCards = new ArrayList<>();

        jsonPlayersArray = jsonObject.getJSONArray("players");
        jsonCommonGoalsArray = jsonObject.getJSONArray("common_goals");

        bag = new Bag();
        board = deserializeBoard(jsonObject.getJSONArray("board").toString(), jsonPlayersArray.length(), bag);
        turnManager = deserializeTurn(jsonObject.toString(), bag);

        for (Object commonGoalCard : jsonCommonGoalsArray) {
            commonGoalCards.add(deserializeCommonGoalCard(commonGoalCard.toString()));
        }

        return new Game.GameBuilder()
                .setCommonGoalCards(commonGoalCards)
                .setTilesBag(bag)
                .setBoard(board)
                .setTurnManager(turnManager)
                .build();
    }

    @Override
    public IPlayer deserializePlayer(String data, IBag bag) {
        JSONObject jsonObject = new JSONObject(data);
        String username = jsonObject.getString("username");
        int score = jsonObject.getInt("score");
        int personalCardId = jsonObject.getInt("personal_card_id");
        IBookshelf bookshelf = deserializeBookshelf(jsonObject.getJSONArray("bookshelf").toString(), bag);

        IPlayer player = new Player(username);
        player.setBookshelf(bookshelf);
        player.setPersonalGoalCard(new PersonalGoalCardDeck().getCardById(personalCardId));
        player.addPointsToScore(score);

        return player;
    }

    @Override
    public IBoard deserializeBoard(String data, int playersCount, IBag bag) {
        JSONArray boardArray = new JSONArray(data);
        TileType[][] boardTiles = deserializeTileGrid(boardArray);

        return new Board.BoardBuilder(playersCount)
                .setTiles(boardTiles, bag)
                .build();
    }

    @Override
    public IBookshelf deserializeBookshelf(String data, IBag bag) {
        JSONArray bookshelfArray = new JSONArray(data);
        TileType[][] bookshelfTiles = deserializeTileGrid(bookshelfArray);

        return new Bookshelf.BookshelfBuilder()
                .setTiles(bookshelfTiles, bag)
                .build();
    }

    private TileType[][] deserializeTileGrid(JSONArray jsonArray) {
        TileType[][] grid = new TileType[jsonArray.length()][jsonArray.getJSONArray(0).length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONArray row = jsonArray.getJSONArray(i);
            for (int j = 0; j < row.length(); j++) {
                int tileOrdinal = row.getInt(j);
                grid[i][j] = null;
                if (tileOrdinal >= 0) grid[i][j] = TileType.values()[tileOrdinal];
            }
        }

        return grid;
    }

    @Override
    public CommonGoalCard deserializeCommonGoalCard(String data) {
        JSONObject jsonObject = new JSONObject(data);
        ICommonGoalCardBuilder builder = new CommonGoalCard.CommonGoalCardBuilder(jsonObject.getInt("card_id"));
        JSONArray players = jsonObject.getJSONArray("completed_by");
        JSONArray points = jsonObject.getJSONArray("points");

        for (int i = 0; i < players.length(); i++) {
            builder.addPlayer(players.getString(i));
        }

        for (int i = 0; i < points.length(); i++) {
            builder.addPoints(points.getInt(i));
        }

        return builder.build();
    }

    @Override
    public ITurnManager deserializeTurn(String data, IBag bag) {
        JSONObject jsonObject = new JSONObject(data);
        ITurnManagerBuilder turnBuilder = new TurnManager.TurnManagerBuilder();

        JSONArray usernames = jsonObject.getJSONArray("players_order");
        JSONArray jsonPlayers = jsonObject.getJSONArray("players");
        List<IPlayer> players = new LinkedList<>();

        for (int i = 0; i < jsonPlayers.length(); i++) {
            players.add(deserializePlayer(jsonPlayers.getJSONObject(i).toString(), bag));
        }

        // add players based on specified order
        for (int i = 0; i < usernames.length(); i++) {
            int currPlayerIndex = players.stream()
                    .map(IPlayer::getUsername)
                    .toList()
                    .indexOf(usernames.getString(i));
            turnBuilder.addPlayer(players.get(currPlayerIndex));
        }

        turnBuilder.setCurrentTurn(jsonObject.getInt("players_turn"));
        turnBuilder.setIsEndGame(jsonObject.getBoolean("is_end_game"));
        return turnBuilder.build();
    }
}
