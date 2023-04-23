package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.server.messages.MessageType;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

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
    }
}
