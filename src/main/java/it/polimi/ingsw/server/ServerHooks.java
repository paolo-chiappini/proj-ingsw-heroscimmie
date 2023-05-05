package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.server.messages.Message;
import org.json.JSONObject;

public abstract class ServerHooks {
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
        res.setBody(message);
    }

    public static void handleJoinGame(Message req, Message res) {
        if (ActiveGameManger.isGameInProgress()) {
            notifyError(res, "Game already in progress, cannot join");
            return;
        }

        JSONObject body = new JSONObject(req.getBody());
        String username = body.getString("username");

        try {
            ActiveGameManger.joinGame(username);
            notifySuccess(res, "Joined game");
        } catch (IllegalActionException iae) {
            notifyError(res, iae.getMessage());
        }
    }

    public static void handleNewGame(Message req, Message res) {
        JSONObject body = new JSONObject(req.getBody());
        int lobbySize = body.getInt("lobby_size");

        try {
            ActiveGameManger.setLobbySize(lobbySize);
            handleJoinGame(req, res);
        } catch (IllegalActionException iae) {
            notifyError(res, iae.getMessage());
        }
    }

    public static void handleGameStart(Message req, Message res) {
        if (ActiveGameManger.canStartGame())  {
            ActiveGameManger.startGame();
            notifyAll(res, "START", "Game started");
        }
    }

    public static void handleSaveGame(Message req, Message res) {
        try {
            String saveDate = ActiveGameManger.saveGame();
            notifyAll(res, "SAVE", "Game successfully saved at " + saveDate);
        } catch (IllegalActionException iae) {
            notifyError(res, iae.getMessage());
        }
    }
}
