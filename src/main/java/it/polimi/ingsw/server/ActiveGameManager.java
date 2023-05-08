package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.bag.Bag;
import it.polimi.ingsw.server.model.bag.IBag;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.IBoard;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Class responsible for managing the active game running on the server.
 */
public abstract class ActiveGameManager {
    private static final int MAX_PLAYERS = 4;
    private static final int MIN_PLAYERS = 2;
    private static Game activeGameInstance = null;
    private static List<String> lobby;
    private static List<String> whitelistedPlayers = null;
    private static Set<String> disconnectedPlayers = new HashSet<>();
    private static int lobbySize;
    private static boolean gameStarted;

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

        IBag bag = new Bag();
        IBoard board = new Board(players.size());
        board.refill(bag);

        return new Game.GameBuilder()
                .setTurnManager(new TurnManager(players))
                .setTilesBag(bag)
                .setBoard(board)
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
     * @param saveName name of the file to load.
     * @param player name of the player trying to load the game.
     * @throws IllegalActionException when player is not whitelisted or when another
     * game is already in progress or being setup.
     */
    public static void loadGame(String saveName, String player) {
        if (isGameInProgress()) {
            throw new IllegalActionException("Cannot load new game while another is in progress");
        }

        if (lobby != null) {
            throw  new IllegalActionException("A game is already being setup by a player");
        }

        String serializedData;
        try {
            serializedData = FileIOManager.readFromFile(saveName + ".json", FilePath.SAVED);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Unable to load " + saveName);
        }

        Game loadedGame = new JsonDeserializer().deserializeGame(serializedData);
        whitelistedPlayers = loadedGame.getPlayers().stream().map(IPlayer::getUsername).toList();

        if (!whitelistedPlayers.contains(player)) {
            throw new IllegalActionException("You are not whitelisted in this game, expected players " + String.join(", ", whitelistedPlayers));
        }

        setLobbySize(whitelistedPlayers.size());
        lobby = new LinkedList<>();
        lobby.add(player);

        activeGameInstance = loadedGame;
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
        gameStarted = true;
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
     * @throws IllegalActionException when:
     * <ul>
     *     <li>No lobby has been found</li>
     *     <li>The active lobby is full</li>
     *     <li>Another player in the lobby has the same name</li>
     *     <li>Trying to join a whitelisted game</li>
     * </ul>
     */
    public static void joinGame(String username) {
        // handle reconnection
        if (disconnectedPlayers.contains(username)) {
            disconnectedPlayers.remove(username);
            return;
        }

        if (lobby == null) {
            throw  new IllegalActionException("No lobby found");
        }

        if (lobby.size() == lobbySize) {
            throw  new IllegalActionException("Lobby is full");
        }

        if (lobby.contains(username)) {
            throw new IllegalActionException("Another player in the lobby has the same username");
        }

        if (whitelistedPlayers != null && !whitelistedPlayers.contains(username)) {
            throw new IllegalActionException("Not in whitelist, expected " + String.join(", ", whitelistedPlayers));
        }

        lobby.add(username);
    }

    /**
     * Remove a player from the game.
     * @param username username of the player that left the game/disconnected.
     * @throws IllegalActionException when no lobby has been found or player is not in a lobby.
     */
    public static void leaveGame(String username) {
        if (lobby == null) {
            throw new IllegalActionException("No lobby found");
        }

        if (!lobby.contains(username)) {
            throw new IllegalActionException("Cannot leave game, you're not in a lobby");
        }

        if (isGameInProgress()) disconnectedPlayers.add(username);
        else lobby.remove(username);

        if (lobby.size() == 0 || disconnectedPlayers.size() == lobbySize) resetGame();
    }

    /**
     * @return the set of disconnected players.
     */
    public static Set<String> getDisconnectedPlayers() {
        return new HashSet<>(disconnectedPlayers);
    }

    /**
     * @return the set of connected players.
     */
    public static Set<String> getConnectedPlayers() {
        if (lobby == null) return new HashSet<>();
        Set<String> connectedPlayers = new HashSet<>(lobby);
        connectedPlayers.removeAll(disconnectedPlayers);
        return connectedPlayers;
    }

    /**
     * Terminates the current game emptying the lobby.
     * @throws IllegalActionException when trying to stop a game that doesn't exist.
     */
    public static void stopGame() {
        if (activeGameInstance == null) {
            throw new IllegalActionException("No active game to stop");
        }

        resetGame();
    }

    private static void resetGame() {
        activeGameInstance = null;
        lobby = null;
        whitelistedPlayers = null;
        disconnectedPlayers = new HashSet<>();
        gameStarted = false;
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
        return lobby != null && lobby.size() == lobbySize && !gameStarted;
    }

    /**
     * Checks if a game is in progress.
     * @return true if a game is in progress.
     */
    public static boolean isGameInProgress() {
        return gameStarted;
    }
}
