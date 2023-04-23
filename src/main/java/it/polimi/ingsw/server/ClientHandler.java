package it.polimi.ingsw.server;

import it.polimi.ingsw.server.messages.Message;
import it.polimi.ingsw.server.messages.MessageProvider;
import it.polimi.ingsw.server.messages.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Contains all the logic responsible for handling a single client connection
 */
public class ClientHandler {
    private final Socket clientSocket;
    private final Server server;
    private final MessageType messageType;

    /**
     * @param clientSocket Socket to handle
     * @param server Reference to the server
     * @param messageType Message format used
     */
    public ClientHandler(Socket clientSocket, Server server, MessageType messageType) {
        this.messageType = messageType;
        this.clientSocket = clientSocket;
        this.server = server;
    }


    /**
     * Starts a new thread to handle the client connection.
     */
    public void start() {
       new Thread(this::handleClient).start();
    }


    private void handleClient() {

        server.getActionOnConnection().accept(clientSocket); //New connection event

        try{
            clientSocket.setKeepAlive(true); //For detecting unexpected disconnections

            var reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (true) {

                String clientData = reader.readLine();

                if(clientData == null){ //Connection is closed by the client
                    server.closeConnection(clientSocket); //Notify server
                    server.getActionOnConnectionClosed().accept(clientSocket); //Connection closed event
                    return;
                }

                var connections = server.getConnections();

                MessageProvider factory = new MessageProvider(messageType);
                Message request = factory.getRequestInstance(clientSocket, clientData);
                Message response = factory.getResponseInstance(clientSocket, connections, clientData);

                String method = request.getMethod();

                var callback = server.getCallback(method);
                var specificMiddleware = server.getMiddleware(method);
                var globalMiddleware = server.getGlobalMiddleware();

                /*
                    Request pipeline:
                    ┌───────────────────────────────────────┐
                    │ Global Middleware                     │
                    │       ┌────────────────────────────┐  │
                    │       │ Specific Middleware        │  │
                    │       │       ┌─────────────────┐  │  │
                    │       │       │ Callback        │  │  │
                    │       │       │                 │  │  │
                    │       │       └─────────────────┘  │  │
                    │       └────────────────────────────┘  │
                    └───────────────────────────────────────┘
                    The specific middleware call is hidden from the user by passing a wrapper Callback
                    to the global middleware. By doing this, there's non need for the user to know
                    exactly how the request pipeline is structured.
                */
                globalMiddleware.apply(request, response, (req, res)->{
                    specificMiddleware.apply(req, res, callback);
                });


            }
        }catch (IOException e) { //When connection is interrupted
            server.closeConnection(clientSocket); //Notify server
            server.getActionOnConnectionLost().accept(clientSocket); //Connection lost event
        }
    }
}
