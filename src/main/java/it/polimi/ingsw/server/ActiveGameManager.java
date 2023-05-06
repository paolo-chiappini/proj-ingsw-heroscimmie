package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.bag.Bag;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.bookshelf.Bookshelf;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.goals.common.CommonGoalCardDeck;
import it.polimi.ingsw.server.model.goals.personal.PersonalGoalCardDeck;
import it.polimi.ingsw.server.model.player.IPlayer;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.turn.TurnManager;
import it.polimi.ingsw.util.FileIOManager;
import it.polimi.ingsw.util.FilePath;
import it.polimi.ingsw.util.serialization.JsonDeserializer;
import it.polimi.ingsw.util.serialization.JsonSerializer;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Class responsible for managing the active game running on the server.
 */
public abstract class ActiveGameManager {
    private static final int MAX_PLAYERS = 4;
    private static final int MIN_PLAYERS = 2;
    private static Game activeGameInstance = null;
    private static List<String> lobby;
    private static int lobbySize;

    /**
     * Setups a brand-new instance of Game based on the players
     * in the lobby.
     * @return a new instance of Game.
     */
    private static Game setupNewGame() {
        PersonalGoalCardDeck personalGoalsDeck = new PersonalGoalCardDeck();
        CommonGoalCardDeck commonGoalsDeck = new CommonGoalCardDeck(lobbySize);
        List<IPlayer> players = new LinkedList<>();

        for (var username : lobby) {
            IPlayer player = new Player(username);
            player.setBookshelf(new Bookshelf());
            player.setPersonalGoalCard(personalGoalsDeck.drawCard());
            players.add(player);
        }

        return new Game.GameBuilder()
                .setTurnManager(new TurnManager(players))
                .setTilesBag(new Bag())
                .setBoard(new Board(players.size()))
                .setCommonGoalCards(commonGoalsDeck.drawCards())
                .build();
    }

    /**
     * Saves the current active game state to a file.
     * @throws IllegalActionException when trying to save a game that does not exist.
     */
    public static void saveGame() {
        if (activeGameInstance == null) {
            throw new IllegalActionException("No active game to save");
        }

        Calendar calendar = Calendar.getInstance();
        long currentTimeMs = calendar.getTimeInMillis();

        String serializedGame = activeGameInstance.serialize(new JsonSerializer());
        String filename = "game_" + currentTimeMs + ".json";

        try {
            FileIOManager.writeToFile(filename, serializedGame, FilePath.SAVED);
        } catch (IOException e) {
            throw new RuntimeException("Unable to save game");
        }
    }

    /**
     * Loads a saved game from file.
     * @param gameName name of the file to load.
     * @throws IllegalActionException when missing players in lobby.
     */
    public static void loadGame(String gameName) {
        String data;
        try {
            data = FileIOManager.readFromFile(gameName + ".json", FilePath.SAVED);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Unable to load game " + gameName);
        }

        JSONObject jsonData = new JSONObject(data);
        JsonDeserializer deserializer = new JsonDeserializer();
        Game game = deserializer.deserializeGame(jsonData.toString());

        List<IPlayer> players = game.getPlayers();
        List<String> missingPlayers = new LinkedList<>();

        // check for missing players
        for (IPlayer player : players) {
            if (!lobby.contains(player.getUsername())) missingPlayers.add(player.getUsername());
        }

        if (missingPlayers.size() > 0) {
            String missingPlayersString = missingPlayers.stream()
                    .reduce("", (accumulator, name) -> name + ", " + accumulator);
            missingPlayersString = missingPlayersString.substring(0, missingPlayersString.lastIndexOf(","));
            throw new IllegalActionException("Unable to load game, missing players in lobby: " + missingPlayersString);
        }

        activeGameInstance = game;
    }

    /**
     * Starts a new game.
     * @throws IllegalActionException when a game is already in progress or
     * the lobby is not full.
     */
    public static void startGame() {
        if (!canStartGame()) {
            throw new IllegalActionException("Cannot start new game, either lobby is not full or a game is already in progress");
        }
        if (activeGameInstance == null) activeGameInstance = setupNewGame();
    }

    /**
     * @return the current game running on the server.
     */
    public static Game getActiveGameInstance() {
        return activeGameInstance;
    }

    /**
     * Joins a player in the lobby.
     * @param username player's username.
     */
    public static void joinGame(String username) {
        if (lobby == null) {
            throw  new IllegalActionException("No lobby found");
        }

        if (lobby.size() == lobbySize) {
            throw  new IllegalActionException("Lobby is full");
        }

        if (lobby.contains(username)) {
            throw new IllegalActionException("Another player in the lobby has the same username");
        }

        lobby.add(username);
    }

    /**
     * Terminates the current game emptying the lobby.
     * @throws IllegalActionException when trying to stop a game that doesn't exist.
     */
    public static void stopGame() {
        if (activeGameInstance == null) {
            throw new IllegalActionException("No active game to stop");
        }

        activeGameInstance = null;
        lobby = new LinkedList<>();
    }

    /**
     * Sets the number of players necessary to start a game.
     * @param size size of the lobby.
     * @throws IllegalActionException when a game is already in progress or when
     * the size is not within the acceptable range.
     */
    public static void setLobbySize(int size) {
        if (lobby != null) {
            throw new IllegalActionException("Cannot create new lobby, game already in progress/setup");
        }

        if (size < MIN_PLAYERS || size > MAX_PLAYERS) {
            throw new IllegalActionException("Invalid size for lobby " + size);
        }

        lobbySize = size;
        lobby = new LinkedList<>();
    }

    /**
     * Check if a game can start.
     * @return true if the game can be started, false if lobby is not full or
     * if a game is already in progress.
     */
    public static boolean canStartGame() {
        return lobby != null && lobby.size() == lobbySize;
    }

    /**
     * Checks if a game is in progress.
     * @return true if a game is in progress.
     */
    public static boolean isGameInProgress() {
        return activeGameInstance != null;
    }
}
