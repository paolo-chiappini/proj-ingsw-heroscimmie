package it.polimi.ingsw.server;

import it.polimi.ingsw.server.messages.Message;
import it.polimi.ingsw.server.messages.MessageProvider;
import it.polimi.ingsw.server.messages.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler {
    private final Socket clientSocket;
    private final Server server;
    private final MessageType messageType;
    public ClientHandler(Socket clientSocket, Server server, MessageType messageType) {
        this.messageType = messageType;
        this.clientSocket = clientSocket;
        this.server = server;
    }

    public void start() {
       new Thread(this::handleClient).start();
    }

    private void handleClient() {

        server.getActionOnConnection().accept(clientSocket); //Connection starts callback

        try{
            clientSocket.setKeepAlive(true);

            var reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (true) {

                String clientData = reader.readLine();

                if(clientData == null){ //Connection is closed by the client
                    server.closeConnection(clientSocket); //Notify server
                    server.getActionOnConnectionClosed().accept(clientSocket); //Connection closed callback
                    return;
                }

                var connections = server.getConnections();
                Message request = MessageProvider.getInstanceOf(messageType, clientSocket, connections, clientData);
                Message response = MessageProvider.getInstanceOf(messageType, clientSocket, connections);

                String method = request.getMethod();

                var callback = server.getCallback(method);
                var specificMiddleware = server.getMiddleware(method);
                var globalMiddleware = server.getGlobalMiddleware();

                //Callback pipeline. The middleware call is hidden from the user
                globalMiddleware.apply(request, response, (req, res)->{
                    specificMiddleware.apply(req, res, callback);
                });


            }
        }catch (IOException e) { //When connection is interrupted
            server.closeConnection(clientSocket); //Notify server
            server.getActionOnConnectionLost().accept(clientSocket); //Connection lost callback
        }
    }
}
