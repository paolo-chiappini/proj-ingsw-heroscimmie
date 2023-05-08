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
import it.polimi.ingsw.server.model.turn.ITurnManager;
import it.polimi.ingsw.util.FileIOManager;
import it.polimi.ingsw.util.FilePath;
import it.polimi.ingsw.util.serialization.JsonSerializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        setupResponse(res, method, message);
        res.send();
    }

    private static void setupResponse(Message res, String method, String message) {
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
        if (missingPropertiesInBody(List.of("username"), req, res)) return;

        JSONObject body = new JSONObject(req.getBody());
        String username = body.getString("username");

        if (ActiveGameManager.isGameInProgress() && !ActiveGameManager.getDisconnectedPlayers().contains(username)) {
            notifyError(res, "Game already in progress, cannot join");
            return;
        }

        try {
            ActiveGameManager.joinGame(username);
            playerSockets.put(req.getSocket(), username);
            notifySuccess(res, "Joined game");

            // in case of reconnection send update to client
            if (ActiveGameManager.isGameInProgress()) {
                JSONObject update = new JSONObject();
                update.put("serialized", new JSONObject(ActiveGameManager.getActiveGameInstance().serialize(jsonSerializer)));
                sendUpdate(res, update);
            }
        } catch (IllegalActionException iae) {
            notifyError(res, iae.getMessage());
        }
    }

    public static void handleNewGame(Message req, Message res) {
        if (missingPropertiesInBody(List.of("username", "lobby_size"), req, res)) return;

        JSONObject body = new JSONObject(req.getBody());
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
            JSONObject body = new JSONObject();
            body.put("serialized", new JSONObject(ActiveGameManager.getActiveGameInstance().serialize(jsonSerializer)));
            res.setMethod("START");
            res.setBody(body.toString());
            res.sendToAll();
        }
    }

    public static void handleLoadGame(Message req, Message res) {
        if (missingPropertiesInBody(List.of("username", "save_name"), req, res)) return;

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
        if (missingPropertiesInBody(List.of("username"), req, res)) return;

        Game game = ActiveGameManager.getActiveGameInstance();
        if (game == null) {
            notifyError(res, "No active game");
            return;
        }

        JSONObject body = new JSONObject(req.getBody());

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
            JSONObject resBody = new JSONObject();
            resBody.put("msg", "Game successfully saved");
            res.setBody(resBody.toString());
            res.setMethod("SAVE");
            res.sendToAll();
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
        if (missingPropertiesInBody(List.of("username", "row1", "col1", "row2", "col2"), req, res)) return;

        Game currentGame = ActiveGameManager.getActiveGameInstance();
        IBoard board = currentGame.getBoard();
        IBookshelf bookshelf;

        JSONObject body = new JSONObject(req.getBody());
        String username = body.getString("username");
        bookshelf = getPlayerByUsername(username, currentGame.getPlayers()).getBookshelf();

        int row1, row2, col1, col2;
        row1 = body.getInt("row1");
        row2 = body.getInt("row2");
        col1 = body.getInt("col1");
        col2 = body.getInt("col2");

        var depths = bookshelfColumnsDepths(bookshelf);
        JSONArray colDepths = new JSONArray(depths);

        if (!board.canPickUpTiles(row1, col1, row2, col2)) {
            notifyError(res, "Invalid tile range");
        } else {
            JSONObject responseBody = new JSONObject();
            responseBody.put("bookshelf_columns_depths", colDepths);
            sendUpdate(res, responseBody);
        }
    }

    public static void handleDropTiles(Message req, Message res) {
        if (missingPropertiesInBody(List.of("username", "tiles", "column", "row1", "row2", "col1", "col2"), req, res)) return;

        Game currentGame = ActiveGameManager.getActiveGameInstance();
        IBookshelf bookshelf;
        IPlayer player;
        String username;
        int row1, row2, col1, col2, col;
        List<GameTile> tiles;

        JSONObject body = new JSONObject(req.getBody());

        username = body.getString("username");
        col = body.getInt("column");
        row1 = body.getInt("row1");
        row2 = body.getInt("row2");
        col1 = body.getInt("col1");
        col2 = body.getInt("col2");

        player = getPlayerByUsername(username, currentGame.getPlayers());
        bookshelf = player.getBookshelf();

        tiles = currentGame.getBoard().pickUpTiles(row1, col1, row2, col2);

        if (!bookshelf.canDropTiles(tiles.size(), col)) {
            notifyError(res, "Cannot drop tiles at specified location");
        } else {
            bookshelf.dropTiles(tiles, col);
            JSONObject update = new JSONObject();
            update.put("serialize", new JSONObject(currentGame.serialize(jsonSerializer)));
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
        update.put("serialized", new JSONObject(game.serialize(jsonSerializer)));
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
            JSONObject dummyRequestBody = new JSONObject();
            dummyRequestBody.put("body", new JSONObject().put("username", player));
            var res = msgProvider.getInstanceForOutgoingResponse(sockets.get(0), sockets);
            var req = msgProvider.getInstanceForIncomingRequest(sockets.get(0), dummyRequestBody.toString());

            handlePlayerLeaving(req, res);
        }
    }

    public static void handlePlayerLeaving(Message req, Message res) {
        JSONObject body = new JSONObject(req.getBody());
        String username = body.getString("username");

        try {
            ActiveGameManager.leaveGame(username);
            res.setMethod("LEFT");
            JSONObject resBody = new JSONObject();
            resBody.put("username", username);
            res.setBody(resBody.toString());
            res.sendToAll();
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

    private static List<String> getMissingProperties(List<String> propertiesToCheck, JSONObject obj) {
        List<String> missingProperties = new LinkedList<>();
        for (String prop : propertiesToCheck) {
            if (!obj.has(prop)) missingProperties.add(prop);
        }
        return  missingProperties;
    }

    private static boolean missingPropertiesInBody(List<String> properties, Message req, Message res) {
        var missingProperties = getMissingProperties(properties, new JSONObject(req.getBody()));
        if (missingProperties.size() > 0) {
            notifyError(res, "Missing properties: " + String.join(", ", missingProperties));
            return true;
        }
        return false;
    }

    private static List<Integer> bookshelfColumnsDepths (IBookshelf bookshelf) {
        List<Integer> depths = new LinkedList<>();
        for (int i = 0; i < bookshelf.getWidth(); i++) {
            int depth = 0;
            for (int j = 0; j < bookshelf.getHeight(); j++) {
                if (bookshelf.getTileAt(i, j) == null) depth++;
                else break;
            }
            depths.add(depth);
        }
        return depths;
    }

    public static boolean validateSocketUsername(Message req, Message res) {
        if (missingPropertiesInBody(List.of("username"), req, res)) return false;
        JSONObject body = new JSONObject(req.getBody());
        String username = body.getString("username");
        if (playerSockets.containsKey(req.getSocket()) && !playerSockets.get(req.getSocket()).equals(username)) {
            notifyError(res, "Username does not match with socket");
            return false;
        }
        return true;
    }
}
