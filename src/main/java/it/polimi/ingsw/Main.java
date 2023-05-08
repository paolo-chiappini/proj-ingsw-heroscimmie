package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.ServerHandlers;
import it.polimi.ingsw.server.messages.MessageType;

public class Main {

    public static void main(String[] args) {
        Server server = new Server(49152, MessageType.JSON);

        server.setCallback("JOIN", ServerHandlers::handleJoinGame);
        server.setCallback("NEW", ServerHandlers::handleNewGame);
        server.setCallback("LOAD", ServerHandlers::handleLoadGame);
        server.setCallback("SAVE", ServerHandlers::handleSaveGame);
        server.setCallback("LIST", ServerHandlers::handleShowSavedGames);
        server.setCallback("QUIT", ServerHandlers::handlePlayerLeaving);
        server.setCallback("PICK_BOARD", ServerHandlers::handleBoardTilePickUp);
        server.setCallback("DROP", ServerHandlers::handleDropTiles);
        server.setCallback("END_TURN", ServerHandlers::handleEndTurn);

        server.onConnectionLost(ServerHandlers::handleDisconnection);

        server.setMiddleware("JOIN", (req, res, next) -> {
            next.call(req, res);
            ServerHandlers.handleGameStart(req, res);
        });

        server.setGlobalMiddleware((req, res, next) -> {
            if (ServerHandlers.validateRequestBody(req, res)) next.call(req, res);
        });

        server.start();

    }
}
