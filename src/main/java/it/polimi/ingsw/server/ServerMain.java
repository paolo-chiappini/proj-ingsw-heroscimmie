package it.polimi.ingsw.server;

import it.polimi.ingsw.server.messages.MessageType;

public abstract class ServerMain {

    public static void main(String[] args) {
        Server server = new Server(49152, MessageType.JSON);

        server.setCallback("JOIN", ServerHandlers::handleJoinGame);
        server.setCallback("NEW",  ServerHandlers::handleNewGame);
        server.setCallback("LOAD", ServerHandlers::handleLoadGame);
        server.setCallback("SAVE", ServerHandlers::handleSaveGame);
        server.setCallback("LIST", ServerHandlers::handleShowSavedGames);
        server.setCallback("QUIT", ServerHandlers::handlePlayerLeaving);
        server.setCallback("PICK", ServerHandlers::handleBoardTilePickUp);
        server.setCallback("DROP", ServerHandlers::handleDropTiles);
        server.setCallback("NEXT", ServerHandlers::handleEndTurn);
        server.setCallback("CHAT", (request, response) -> {
            System.out.println(request.getBody());
            response.setBody(request.getBody());
            response.setMethod("CHAT");
            response.sendToAll();
        });


        server.onConnectionLost(ServerHandlers::handleDisconnection);
        server.onConnectionClosed(ServerHandlers::handleDisconnection);

        server.setMiddleware("JOIN", (req, res, callback) -> {
            callback.call(req, res);
            ServerHandlers.handleGameStart(req, res);
        });

        server.setGlobalMiddleware((req, res, next) -> {
            if (ServerHandlers.validateRequestBody(req, res) && ServerHandlers.validateSocketUsername(req, res)) {
                next.call(req, res);
            }
        });

        server.start();

    }
}
