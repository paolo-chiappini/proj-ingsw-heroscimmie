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

    public static void notifyMessage(Message res, String method, String message) {
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
        var disconnectedPlayers = ActiveGameManager.getDisconnectedPlayers().toArray();
        var connectedPlayers = ActiveGameManager.getConnectedPlayers().toArray();
        update.put("disconnected_players", new JSONArray(disconnectedPlayers));
        update.put("connected_players", new JSONArray(connectedPlayers));

        // DEBUG
        System.out.println("Sending update: " + update);

        response.setBody(update.toString());
        response.sendToAll();
    }

    /**
     * Handles when a player tries to join a game.
     * @param req request message
     * @param res response message
     */
    public static void handleJoinGame(Message req, Message res) {
        if (missingPropertiesInBody(List.of("username"), req, res)) return;

        JSONObject body = new JSONObject(req.getBody());
        String username = body.getString("username");

        if (username.isEmpty()) {
            notifyError(res, "Invalid username, username cannot be empty");
            return;
        }

        if (ActiveGameManager.isGameInProgress() && !ActiveGameManager.getDisconnectedPlayers().contains(username)) {
            notifyError(res, "Game already in progress, cannot join");
            return;
        }

        try {
            ActiveGameManager.joinGame(username);
            notifySuccess(res, "Joined game");

            // in case of reconnection send update to client
            if (ActiveGameManager.isGameInProgress()) {
                JSONObject update = new JSONObject();
                update.put("serialized", new JSONObject(ActiveGameManager.getActiveGameInstance().serialize(jsonSerializer)));
                update.put("reconnected", true);
                sendUpdate(res, update);
            }
        } catch (IllegalActionException iae) {
            notifyError(res, iae.getMessage());
        }
    }

    /**
     * Handles the creation of a new game.
     * @param req request message
     * @param res response message
     */
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

    /**
     * Handles the start of a new game.
     * @param ignore ignored request message
     * @param res response message
     */
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

    /**
     * Handles the loading of a saved game.
     * @param req request message
     * @param res response message
     */
    public static void handleLoadGame(Message req, Message res) {
        if (missingPropertiesInBody(List.of("username", "save_index"), req, res)) return;

        JSONObject body = new JSONObject(req.getBody());
        int saveIndex = body.getInt("save_index");
        String username = body.getString("username");

        try {
            var files = FileIOManager.getFilesInDirectory(FilePath.SAVED.getPath());
            ActiveGameManager.loadGame(files.get(saveIndex), username);
            notifySuccess(res, "Successfully loaded game");
        } catch (RuntimeException re) {
            notifyError(res, re.getMessage());
        }
    }

    /**
     * Handles the saving of a game instance.
     * @param req request message
     * @param res response message
     */
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
        } catch (RuntimeException iae) {
            notifyError(res, iae.getMessage());
        }
    }


    /**
     * Sends to the client the list of all saved games.
     * @param req request message
     * @param res response message
     */
    public static void handleShowSavedGames(Message req, Message res) {
        List<String> files = null;
        try {
            files = FileIOManager.getFilesInDirectory(FilePath.SAVED.getPath());
        } catch (RuntimeException ignored) {}
        JSONArray jsonFiles = new JSONArray();
        JSONObject body = new JSONObject();

        res.setMethod(req.getMethod());

        if (files != null) jsonFiles = new JSONArray(files);

        body.put("files", jsonFiles);
        res.setBody(body.toString());
        res.send();
    }

    /**
     * Validates a request body by checking if the format is correct.
     * @param req request message
     * @param res response message
     * @return true if the message body is in JSON format, false otherwise.
     */
    public static boolean validateRequestBody(Message req, Message res) {
        try {
            new JSONObject(req.getBody());
            return true;
        } catch (Exception e) {
            notifyError(res, "Invalid request body format, should be JSON");
            return false;
        }
    }

    /**
     * Handles the picking of tiles from the board.
     * Does not respond with an update.
     * @param req request message
     * @param res response message
     */
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

        int numberOfTilesPickedUp = Integer.max(Math.abs(row2-row1),Math.abs(col2-col1));
        List<Integer> depths = bookshelfColumnsDepths(bookshelf);

        try {
            if (!board.canPickUpTiles(row1, col1, row2, col2)) {
                notifyError(res, "Invalid tile range");
            }
            else {
                if(depths.stream().noneMatch(depth -> depth > numberOfTilesPickedUp))
                    notifyError(res,"There aren't enough free spaces in the bookshelf to pick up these tiles");
            }
        } catch (RuntimeException re) {
            notifyError(res, re.getMessage());
        }
    }

    /**
     * Handles the dropping of tiles in the bookshelf.
     * Sends the updated state.
     * @param req request message
     * @param res response message
     */
    public static void handleDropTiles(Message req, Message res) {
        if (missingPropertiesInBody(List.of("username", "column", "row1", "row2", "col1", "col2", "first_tile", "second_tile", "third_tile"), req, res)) return;

        Game currentGame = ActiveGameManager.getActiveGameInstance();
        IBookshelf bookshelf;
        IPlayer player;
        List<GameTile> tiles;

        JSONObject body = new JSONObject(req.getBody());
        String username = body.getString("username");

        int row1, row2, col1, col2, col, first, second, third;
        col = body.getInt("column");
        row1 = body.getInt("row1");
        row2 = body.getInt("row2");
        col1 = body.getInt("col1");
        col2 = body.getInt("col2");
        first = body.getInt("first_tile");
        second = body.getInt("second_tile");
        third = body.getInt("third_tile");

        player = getPlayerByUsername(username, currentGame.getPlayers());
        bookshelf = player.getBookshelf();

        try {
            if (!currentGame.getBoard().canPickUpTiles(row1, col1, row2, col2)) {
                notifyError(res, "Something went wrong, cannot pick up selected tiles");
                return;
            }

            int numberOfTilesToPickUp = Integer.max(Math.abs(row2 - row1), Math.abs(col2- col1)) + 1;
            if (!bookshelf.canDropTiles(numberOfTilesToPickUp, col)) {
                notifyError(res, "Cannot drop tiles at specified location");
            }
            else {
                tiles = currentGame.getBoard().pickUpTiles(row1, col1, row2, col2);
                bookshelf.dropTiles(bookshelf.decideTilesOrder(tiles,first,second,third), col);
                handleEndTurn(req, res);
            }
        } catch (RuntimeException re) {
            notifyError(res, re.getMessage());
        }
    }

    /**
     * Handles the end of a player's turn.
     * Sends the updated state.
     * @param req request message
     * @param res response message
     */
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
            if (commonGoal.canObtainPoints(player.getBookshelf()) &&
                    !commonGoal.getAwardedPlayers().contains(player.getUsername()))
                player.addPointsToScore(commonGoal.evaluatePoints(player));
        }

        try {
            var disconnectedPlayers = ActiveGameManager.getDisconnectedPlayers();
            // Skip turns until the first player that can play or until game is over
            do {
                turnManager.nextTurn();
            } while (
                    disconnectedPlayers.contains(turnManager.getCurrentPlayer().getUsername())
                    && !turnManager.isGameOver()
            );

        } catch (IllegalActionException iae) {
            notifyError(res, iae.getMessage());
        }

        JSONObject update = new JSONObject();
        JSONObject serializedGame = new JSONObject(game.serialize(jsonSerializer));

        if (turnManager.isGameOver() && ActiveGameManager.getConnectedPlayers().size() > 1) {
            game.evaluateFinalScores();
            // Re-serialize game with updated scores
            serializedGame = new JSONObject(game.serialize(jsonSerializer));
            update.put("winner", game.getWinner().getUsername());
        } else if (ActiveGameManager.getConnectedPlayers().size() == 1) {
            serializedGame.put("is_end_game", true);
            var lastPlayer = ActiveGameManager.getConnectedPlayers().toArray();
            if (lastPlayer.length == 1) {
                update.put("winner", lastPlayer[0]);
            }
        }

        update.put("serialized", serializedGame);
        sendUpdate(res, update);

        // Stop must be done after update or players won't be notified
        // of others disconnecting.
        if (turnManager.isGameOver() || ActiveGameManager.getConnectedPlayers().size() == 1) {
            ActiveGameManager.stopGame();
        }
    }

    /**
     * Finds the corresponding player object given the username.
     *
     * @param username player's username to find
     * @param players  list of players to search
     * @return the player object with the requested username. Returns null if no
     * object with the specified username is found.
     */
    private static IPlayer getPlayerByUsername(String username, List<IPlayer> players) {
        for (IPlayer player : players)
            if (player.getUsername().equals(username)) return player;
        return null;
    }

    /**
     * Handles the disconnection of a socket.
     *
     * @param socket disconnected socket
     */
    public static void handleDisconnection(Socket socket) {
        System.out.println(socket.getInetAddress() + " disconnected");
        if (playerSockets.containsKey(socket)) {
            String player = playerSockets.remove(socket);

            var sockets = playerSockets.keySet().stream().toList();
            if (sockets.size() == 0) {
                if (ActiveGameManager.isGameInProgress()) ActiveGameManager.stopGame();
                return;
            }

            MessageProvider msgProvider = new MessageProvider(MessageType.JSON);
            JSONObject dummyRequestBody = new JSONObject();

            dummyRequestBody.put("body", new JSONObject().put("username", player));

            var res = msgProvider.getInstanceForOutgoingResponse(sockets.get(0), sockets);
            var req = msgProvider.getInstanceForIncomingRequest(sockets.get(0), dummyRequestBody.toString());

            handlePlayerLeaving(req, res);
        }
    }

    /**
     * Handles when a player disconnects from/leaves the game.
     * @param req request message
     * @param res response message
     */
    public static void handlePlayerLeaving(Message req, Message res) {
        JSONObject body = new JSONObject(req.getBody());
        String username = body.getString("username");
        Game game = ActiveGameManager.getActiveGameInstance();

        try {
            ActiveGameManager.leaveGame(username);
        } catch (IllegalActionException ignored) {}

        if (ActiveGameManager.getConnectedPlayers().isEmpty() && ActiveGameManager.isGameInProgress()) {
            System.out.println("No more players, resetting");
            ActiveGameManager.stopGame();
        }
        // Check if there's at least one player still in-game
        else if (ActiveGameManager.isGameInProgress() && ActiveGameManager.getConnectedPlayers().size() >= 1) {
            boolean isDisconnectPlayerTurn = game.getTurnManager()
                    .getCurrentPlayer()
                    .getUsername()
                    .equals(username);
            boolean onePlayerLeft = ActiveGameManager.getConnectedPlayers().size() == 1;

            if (isDisconnectPlayerTurn || onePlayerLeft) handleEndTurn(req, res);
            else sendUpdate(res, new JSONObject().put("serialized", new JSONObject(game.serialize(jsonSerializer))));
        }
    }

    /**
     * Support method used to get all missing properties in a json object.
     *
     * @param propertiesToCheck list of names of properties to check
     * @param obj               json object to verify
     * @return the list of missing properties expected in said object.
     */
    private static List<String> getMissingProperties(List<String> propertiesToCheck, JSONObject obj) {
        List<String> missingProperties = new LinkedList<>();
        for (String prop : propertiesToCheck) {
            if (!obj.has(prop)) missingProperties.add(prop);
        }
        return missingProperties;
    }

    /**
     * Check whether the body of a request contains all expected properties.
     *
     * @param properties list of expected properties
     * @param req        request message
     * @param res        response message
     * @return returns true if there are missing properties in the request body, also notifies the client
     * with a message. Returns false if all expected properties have been found in the request body.
     */
    private static boolean missingPropertiesInBody(List<String> properties, Message req, Message res) {
        var missingProperties = getMissingProperties(properties, new JSONObject(req.getBody()));
        if (missingProperties.size() > 0) {
            notifyError(res, "Missing properties: " + String.join(", ", missingProperties));
            return true;
        }
        return false;
    }

    /**
     * Method used to determine the maximum amount of tiles that
     * can be inserted in the bookshelf.
     * @param bookshelf bookshelf to evaluate
     * @return the depths of the columns of the bookshelf.
     */
    private static List<Integer> bookshelfColumnsDepths (IBookshelf bookshelf) {
        List<Integer> depths = new LinkedList<>();
        for (int i = 0; i < bookshelf.getWidth(); i++) {
            int depth = 0;
            for (int j = 0; j < bookshelf.getHeight(); j++) {
                if (bookshelf.getTileAt(j, i) == null) depth++;
                else break;
            }
            depths.add(depth);
        }
        return depths;
    }

    /**
     * Validates the incoming request by checking if the player that
     * sent the message is impersonating another client.
     * Used to prevent "sabotage" through impersonation of other clients:
     * ie: (player1) -> {"method": "quit", "username": "player3"} would lead to the disconnection of
     * player3, but the message was sent by player1.
     * @param req request message
     * @param res response message
     * @return true if the client's username matches the bound socket, false
     * if the client is trying to impersonate another player.
     */
    public static boolean validateSocketUsername(Message req, Message res) {
        if (missingPropertiesInBody(List.of("username"), req, res)) return false;

        // demand validation to name change handler
        if (req.getMethod().equals("NAME")) return true;

        JSONObject body = new JSONObject(req.getBody());
        String username = body.getString("username");

        if (playerSockets.containsKey(req.getSocket()) && !playerSockets.get(req.getSocket()).equals(username)) {
            notifyError(res, "Username does not match with socket");
            return false;
        }
        return true;
    }

    /**
     * Handles the assignment of names to the clients.
     * Necessary in case a user wants to change username while connected to the server.
     * @param req request message
     * @param res response message
     */
    public static void handlePlayerNameChange(Message req, Message res) {
        String username = new JSONObject(req.getBody()).getString("username");
        if (playerSockets.containsValue(username)) {
            // check if name is already associated with user
            if (!(playerSockets.containsKey(req.getSocket()) && playerSockets.get(req.getSocket()).equals(username)))
                notifyError(res, "Another user has chosen this name");
        } else {
            playerSockets.put(req.getSocket(), username);
            res.setMethod("NAME");
            res.setBody(new JSONObject(req.getBody()).toString());
            res.send();
        }
    }
}
