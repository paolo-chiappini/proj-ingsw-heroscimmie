package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.messages.MessageType;

public class Main {
    public static void main(String[] args) {
        Server s = new Server(42069, MessageType.JSON);

        s.onConnection((client)->{
            var address  = client.getRemoteSocketAddress();
            System.out.println("Si è connesso " + address);
        });

        s.onConnectionLost((c)->{
            System.out.println("Client "+c.getRemoteSocketAddress()+" lost connection");
        });

        s.onConnectionClosed((c)->{
            System.out.println("Client "+c.getRemoteSocketAddress()+" closed the connection to the server");
        });

        s.setCallback("HELO", (req, res)->{
            res.setBody("ZAO CORE");
            res.send();
        });

        s.setGlobalMiddleware((req, res, next)->{
            res.setMethod("RES");
            next.call(req, res);
        });

        s.start();
    }
}
