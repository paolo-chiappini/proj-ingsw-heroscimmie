package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.ServerHooks;
import it.polimi.ingsw.server.messages.MessageType;
import it.polimi.ingsw.server.model.game.Game;
import it.polimi.ingsw.server.ActiveGameManger;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*
        // Giusto per avere un esempio di utilizzo:
        // server molto gentile che saluta
        Server s = new Server(42069, MessageType.JSON);

        s.onConnection((client)->{
            var address  = client.getRemoteSocketAddress();
            System.out.println("New connection: " + address);
        });

        s.onConnectionLost((c)->{
            System.out.println("Lost connection to "+c.getRemoteSocketAddress());
        });

        s.onConnectionClosed((c)->{
            System.out.println("Client "+c.getRemoteSocketAddress()+" closed the connection to the server");
        });

        s.setDefaultCallback((req, res)->{
            res.setBody("Se vuoi dirmi qualcosa, usa il metodo \"HELO\"");
            res.send();
        });

        s.setCallback("HELO", (req, res)->{
            res.setBody("BELANDI");
            res.send();
        });

        s.setGlobalMiddleware((req, res, next)->{
            res.setMethod("RES"); //Così il client può distinguere eventuali tipi di risposta
            next.call(req, res);
        });

        s.start();*/

        Server server = new Server(49152, MessageType.JSON);

        server.setCallback("JOIN", ServerHooks::handleJoinGame);
        server.setCallback("NEWG", ServerHooks::handleNewGame);
        server.setCallback("SAVE", ServerHooks::handleSaveGame);

        server.setMiddleware("JOIN", (req, res, next) -> {
            next.call(req, res);
            ServerHooks.handleGameStart(req, res);
        });

        server.start();

        /*List<String> connectedPlayers = List.of("Tizio", "Caio", "Sempronio");

        ActiveGameManger.setLobbySize(connectedPlayers.size());
        for (var name : connectedPlayers) {
            ActiveGameManger.joinGame(name);
        }

        ActiveGameManger.loadGame("game_1683281996655");
        Game game = ActiveGameManger.startGame();
        ActiveGameManger.dumpGame(game);

        ActiveGameManger.loadGame("game_1683277888287");
        game = ActiveGameManger.canStartGame() ? ActiveGameManger.startGame() : null;
        assert game != null;
        ActiveGameManger.dumpGame(game);*/
    }
}
