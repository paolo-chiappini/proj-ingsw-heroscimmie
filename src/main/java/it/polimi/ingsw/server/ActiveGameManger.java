package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.bag.Bag;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.bookshelf.Bookshelf;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.goals.common.CommonGoalCard;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public abstract class ActiveGameManger {
    private static final int MAX_PLAYERS = 4;
    private static final int MIN_PLAYERS = 2;
    private static Game activeGameInstance = null;
    private static List<String> lobby;
    private static int lobbySize;

    public ActiveGameManger() {}

    private static Game setupNewGame() {
        PersonalGoalCardDeck personalGoalsDeck = new PersonalGoalCardDeck();
        List<IPlayer> players = new LinkedList<>();

        for (var username : lobby) {
            IPlayer player = new Player(username);
            player.setBookshelf(new Bookshelf());
            player.setPersonalGoalCard(personalGoalsDeck.drawCard());
            players.add(player);
        }

        return new Game (
                new TurnManager(players),
                new Bag(),
                new Board(players.size())
        );
    }

    public static String saveGame() {
        if (activeGameInstance == null) {
            throw new IllegalActionException("No active game to save");
        }

        Calendar calendar = Calendar.getInstance();
        long currentTimeMs = calendar.getTimeInMillis();
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");

        JSONObject savedGame = new JSONObject();
        String saveDate = formatter.format(new Date(currentTimeMs));
        savedGame.put("save_date", saveDate);

        String serializedGame = activeGameInstance.serialize(new JsonSerializer());
        savedGame.put("game_data", new JSONObject(serializedGame));

        String filename = "game_" + currentTimeMs;

        try {
            FileIOManager.writeToFile(filename, savedGame.toString(), FilePath.SAVED);
        } catch (IOException e) {
            throw new RuntimeException("Unable to save game");
        }

        return saveDate;
    }

    public static void loadGame(String gameName) {
        String data;
        try {
            data = FileIOManager.readFromFile(gameName, FilePath.SAVED);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Unable to load game " + gameName);
        }

        JSONObject jsonData = new JSONObject(data);
        JsonDeserializer deserializer = new JsonDeserializer();
        Game game = deserializer.deserializeGame(jsonData.getJSONObject("game_data").toString());

        List<IPlayer> players = game.getPlayers();
        List<String> missingPlayers = new LinkedList<>();
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

    public static Game startGame() {
        if (!canStartGame()) {
            throw new IllegalActionException("Cannot start new game, either lobby is not full or a game is already in progress");
        }
        if (activeGameInstance == null) activeGameInstance = setupNewGame();
        return activeGameInstance;
    }

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

    public static void stopGame() {
        if (activeGameInstance == null) {
            throw new IllegalActionException("No active game to stop");
        }

        activeGameInstance = null;
        lobby = new LinkedList<>();
    }

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

    public static boolean canStartGame() {
        return lobby != null && lobby.size() == lobbySize;
    }

    public static boolean isGameInProgress() {
        return activeGameInstance != null;
    }

    public static void dumpGame(Game game) {
        var players = game.getPlayers();
        for (IPlayer player : players) {
            System.out.println("Player " + player.getUsername() + ", score " + player.getScore() + ", personal card " + player.getPersonalGoalCard().getId());
        }

        var commonGoals = game.getCommonGoals();
        for (CommonGoalCard commonGoal : commonGoals) {
            System.out.println("Common goal " + commonGoal.getId());
        }
    }
}
