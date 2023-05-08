package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.server.messages.Message;
import it.polimi.ingsw.server.messages.MessageProvider;
import it.polimi.ingsw.server.messages.MessageType;
import it.polimi.ingsw.server.model.bag.IBag;
import it.polimi.ingsw.server.model.board.IBoard;
import it.polimi.ingsw.server.model.bookshelf.IBookshelf;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.model.goals.common.CommonGoalCard;
import it.polimi.ingsw.server.model.player.IPlayer;
import it.polimi.ingsw.server.model.tile.GameTile;
import it.polimi.ingsw.server.model.tile.Tile;
import it.polimi.ingsw.server.model.tile.TileType;
import it.polimi.ingsw.server.model.turn.ITurnManager;
import it.polimi.ingsw.util.FileIOManager;
import it.polimi.ingsw.util.FilePath;
import it.polimi.ingsw.util.serialization.JsonSerializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TO DO: methods are missing checks on fields.
// TO DO: check if username in request matches socket.
public abstract class ServerHandlers {
    private static final JsonSerializer jsonSerializer = new JsonSerializer();
    private static final Map<Socket, String> playerSockets = new HashMap<>();

    public static void notifyError(Message res, String errorMessage) {
        notifyMessage(res, "ERR", errorMessage);
    }

    public static void notifySuccess(Message res, String successMessage) {
        notifyMessage(res, "OK", successMessage);
    }

    private static void notifyMessage(Message res, String method, String message) {
        setupRes(res, method, message);
        res.send();
    }

    private static void notifyAll(Message res, String method, String message) {
        setupRes(res, method, message);
        res.sendToAll();
    }

    private static void setupRes(Message res, String method, String message) {
        res.setMethod(method);
        if (message == null) message = "{}";
        res.setBody(String.format("{msg: \"%s\"}", message));
    }

    private static void sendUpdate(Message response, JSONObject update) {
        response.setMethod("UPDATE");
        response.setBody(update.toString());
        response.sendToAll();
    }

    public static void handleJoinGame(Message req, Message res) {
        JSONObject body = new JSONObject(req.getBody());

        if (!body.has("username")) {
            notifyError(res, "Missing field username");
            return;
        }
        String username = body.getString("username");

        if (ActiveGameManager.isGameInProgress() && !ActiveGameManager.getDisconnectedPlayers().contains(username)) {
            notifyError(res, "Game already in progress, cannot join");
            return;
        }

        try {
            ActiveGameManager.joinGame(username);
            playerSockets.put(req.getSocket(), username);
            notifySuccess(res, "Joined game");
        } catch (IllegalActionException iae) {
            notifyError(res, iae.getMessage());
        }
    }

    public static void handleNewGame(Message req, Message res) {
        JSONObject body = new JSONObject(req.getBody());

        if (!body.has("lobby_size")) {
            notifyError(res, "Missing field lobby_size");
            return;
        }
        int lobbySize = body.getInt("lobby_size");

        try {
            ActiveGameManager.setLobbySize(lobbySize);
            handleJoinGame(req, res);
        } catch (IllegalActionException iae) {
            notifyError(res, iae.getMessage());
        }
    }

    public static void handleGameStart(Message ignore, Message res) {
        if (ActiveGameManager.canStartGame())  {
            ActiveGameManager.startGame();
            notifyAll(res, "START", "Game started");
        }
    }

    public static void handleLoadGame(Message req, Message res) {
        JSONObject body = new JSONObject(req.getBody());
        String saveName = body.getString("save_name");
        String username = body.getString("username");

        try {
            ActiveGameManager.loadGame(saveName, username);
            notifySuccess(res, "Loaded game " + saveName);
        } catch (IllegalActionException iae) {
            notifyError(res, iae.getMessage());
        }
    }

    public static void handleSaveGame(Message req, Message res) {
        Game game = ActiveGameManager.getActiveGameInstance();
        if (game == null) {
            notifyError(res, "No active game");
            return;
        }

        JSONObject body = new JSONObject(req.getBody());
        if (!body.has("username")) {
            notifyError(res, "Missing field username");
            return;
        }

        String username = body.getString("username");
        if (!game.getPlayers().stream()
                .map(IPlayer::getUsername)
                .toList()
                .contains(username)) {
            notifyError(res, "You are not in a game");
            return;
        }

        try {
            ActiveGameManager.saveGame();
            notifyAll(res, "SAVE", "Game successfully saved");
        } catch (IllegalActionException iae) {
            notifyError(res, iae.getMessage());
        }
    }


    public static void handleShowSavedGames(Message req, Message res) {
        var files = FileIOManager.getFilesInDirectory(FilePath.SAVED);

        res.setMethod(req.getMethod());
        if (files == null) res.setBody("[]");
        else {
            JSONArray jsonFiles = new JSONArray(files);
            JSONObject body = new JSONObject();
            body.put("files", jsonFiles);
            res.setBody(body.toString());
        }

        res.send();
    }

    public static boolean validateRequestBody(Message req, Message res) {
        try {
            new JSONObject(req.getBody());
            return true;
        } catch (Exception e) {
            notifyError(res, "Invalid request body format, should be JSON");
            return false;
        }
    }

    public static void handleBoardTilePickUp(Message req, Message res) {
        Game currentGame = ActiveGameManager.getActiveGameInstance();
        IBoard board = currentGame.getBoard();

        JSONObject body = new JSONObject(req.getBody());

        if (!body.has("row1") || !body.has("row2") ||
            !body.has("col1") || !body.has("col2")) {
            notifyError(res, "Missing range parameter");
            return;
        }

        if (!body.has("username")) {
            notifyError(res, "Missing player username");
            return;
        }

        int row1, row2, col1, col2;
        row1 = body.getInt("row1");
        row2 = body.getInt("row2");
        col1 = body.getInt("col1");
        col2 = body.getInt("col2");

        if (!board.canPickUpTiles(row1, col1, row2, col2)) {
            notifyError(res, "Invalid tile range");
        } else {
            List<GameTile> tiles = board.pickUpTiles(row1, col1, row2, col2);
            JSONArray tilesArray = new JSONArray();
            for (GameTile tile : tiles) tilesArray.put(tile.getType().ordinal());

            JSONObject responseBody = new JSONObject();
            responseBody.put("serialized", currentGame.serialize(jsonSerializer));
            responseBody.put("picked_tiles", tilesArray);
            sendUpdate(res, responseBody);
        }
    }

    public static void handleDropTiles(Message req, Message res) {
        Game currentGame = ActiveGameManager.getActiveGameInstance();
        IBookshelf bookshelf;
        IPlayer player;
        String username;
        int col;
        JSONArray tilesArray;
        List<GameTile> tiles = new ArrayList<>();

        JSONObject body = new JSONObject(req.getBody());

        if (!body.has("username")) {
            notifyError(res, "Missing player username");
            return;
        }

        // TO DO
        // Check other fields

        username = body.getString("username");
        tilesArray = body.getJSONArray("tiles");
        col = body.getInt("column");

        player = getPlayerByUsername(username, currentGame.getPlayers());

        bookshelf = player.getBookshelf();

        for (int i = 0; i < tilesArray.length(); i++) {
            tiles.add(new Tile(TileType.values()[tilesArray.getInt(i)]));
        }

        if (!bookshelf.canDropTiles(tiles.size(), col)) {
            notifyError(res, "Cannot drop tiles at specified location");
        } else {
            bookshelf.dropTiles(tiles, col);
            JSONObject update = new JSONObject();
            update.put("serialize", currentGame.serialize(jsonSerializer));
            sendUpdate(res, update);
        }
    }

    public static void handleEndTurn(Message req, Message res) {
        JSONObject body = new JSONObject(req.getBody());

        Game game = ActiveGameManager.getActiveGameInstance();
        IBoard board = game.getBoard();
        IBag bag = game.getBag();
        ITurnManager turnManager = game.getTurnManager();
        List<CommonGoalCard> commonGoals = game.getCommonGoals();
        String username = body.getString("username");
        IPlayer player = getPlayerByUsername(username, game.getPlayers());

        if (board.needsRefill()) board.refill(bag);

        for (CommonGoalCard commonGoal : commonGoals) {
            player.addPointsToScore(commonGoal.evaluatePoints(player));
        }

        turnManager.nextTurn();

        JSONObject update = new JSONObject();
        update.put("serialized", game.serialize(jsonSerializer));
        sendUpdate(res, update);
    }

    private static IPlayer getPlayerByUsername(String username, List<IPlayer> players) {
        for (IPlayer player : players)
            if (player.getUsername().equals(username)) return player;
        return null;
    }

    public static void handleDisconnection(Socket socket) {
        System.out.println(socket.getInetAddress() + " disconnected");
        if (playerSockets.containsKey(socket)) {
            String player = playerSockets.remove(socket);

            var sockets = playerSockets.keySet().stream().toList();
            if (sockets.size() == 0) return;

            MessageProvider msgProvider = new MessageProvider(MessageType.JSON);
            var res = msgProvider.getInstanceForOutgoingResponse(sockets.get(0), sockets);

            JSONObject dummyRequestBody = new JSONObject();
            dummyRequestBody.put("body", new JSONObject().put("username", player));
            handlePlayerLeaving(
                    msgProvider.getInstanceForIncomingRequest(sockets.get(0), dummyRequestBody.toString()),
                    res
            );

            notifyAll(res, "LEFT",player + " left the game");
        }
    }

    public static void handlePlayerLeaving(Message req, Message res) {
        JSONObject body = new JSONObject(req.getBody());
        String username = body.getString("username");

        try {
            ActiveGameManager.leaveGame(username);
        } catch (IllegalActionException iae) {
            return;
        }

        if (ActiveGameManager.isGameInProgress() && ActiveGameManager.getConnectedPlayers().size() == 1) {
            JSONObject resBody = new JSONObject();
            resBody.put("winner", ActiveGameManager.getConnectedPlayers().stream().findFirst().orElse(""));

            ActiveGameManager.stopGame();
            sendUpdate(res, resBody);
        }
    }
}
