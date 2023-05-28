package it.polimi.ingsw.server;

import it.polimi.ingsw.server.messages.MessageType;

public class ServerMain {

    public static void main(String[] args) {
        Server server = new Server(49152, MessageType.JSON);

        server.setCallback("JOIN", ServerHandlers::handleJoinGame);
        server.setCallback("NEW", ServerHandlers::handleNewGame);
        server.setCallback("LOAD", ServerHandlers::handleLoadGame);
        server.setCallback("SAVE", ServerHandlers::handleSaveGame);
        server.setCallback("LIST", ServerHandlers::handleShowSavedGames);
        server.setCallback("QUIT", ServerHandlers::handlePlayerLeaving);
        server.setCallback("PICK", ServerHandlers::handleBoardTilePickUp);
        server.setCallback("DROP", ServerHandlers::handleDropTiles);
        server.setCallback("NAME", ServerHandlers::handlePlayerNameChange);
        server.setCallback("CHAT", (req, res) -> {
            if (ServerHandlers.validateRequestBody(req, res) && ServerHandlers.validateSocketUsername(req, res)) {
                res.setBody(req.getBody());
                res.setMethod(req.getMethod());
                res.sendToAll();
            }
        });


        //I differentiated between handleDisconnection and handleConnectionClosed just for
        //debugging purposes
        server.onConnectionLost(ServerHandlers::handleDisconnection);
        server.onConnectionClosed(ServerHandlers::handleConnectionClosed);
        server.onConnection(ServerHandlers::handleConnection);

        server.setMiddleware("JOIN", (req, res, callback) -> {
            callback.call(req, res);
            ServerHandlers.handleGameStart(req, res);
        });

        server.setGlobalMiddleware((req, res, next) -> {
//            System.out.println("Request from "+req.getSocket().getRemoteSocketAddress());
//            System.out.println("METHOD: "+req.getMethod());
//            System.out.println("BODY:\n"+req.getBody());
            if (ServerHandlers.validateRequestBody(req, res) && ServerHandlers.validateSocketUsername(req, res)) {
                next.call(req, res);
            }

//            System.out.println("Response to "+res.getSocket().getRemoteSocketAddress());
//            System.out.println("METHOD: "+res.getMethod());
//            System.out.println("BODY:\n"+res.getBody());
        });

        server.start();
    }
}