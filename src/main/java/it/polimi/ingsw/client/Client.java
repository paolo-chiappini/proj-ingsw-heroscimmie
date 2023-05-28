package it.polimi.ingsw.client;

import it.polimi.ingsw.server.messages.Message;
import it.polimi.ingsw.server.messages.MessageProvider;
import it.polimi.ingsw.server.messages.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Handles the connection and disconnection of the client and its communication with the server
 */
public class Client {

    private Socket socket;
    private BufferedReader fromServer;

    protected Message message;
    private final MessageProvider messageProvider = new MessageProvider(MessageType.JSON);
    private Consumer<Message> callback;
    private Consumer<String> connectionLostCallback;
    private static final int SERVER_PORT=49152;
    private boolean isAlive;
    private boolean stoppedByTheUser;
    private final String serverAddress;

    public Client(String address){
        serverAddress = address;

        // default callback
        callback = (msg) -> {};
    }

    /**
     * Sends a request to the server.
     * @param method is the header of the message to send to the server
     * @param body is the body of the message to send to the server
     */
    public void sendRequest(String method, String body) {
        try {
            Message request = messageProvider.getEmptyInstance(socket);
            request.setMethod(method);
            request.setBody(body);
            request.send();
        } catch (RuntimeException re) {
            endConnection();
        }
    }

    /**
     * Read responses from the server.
     */
    public Message readFromServer() {
        try {
            return messageProvider.getInstanceForIncomingRequest(this.socket, fromServer.readLine());
        } catch (IOException e) {
            endConnection();
        }
        return null;
    }


    /**
     * Stops the client in the most polite way possible
     */
    public void closeConnection() {
        stoppedByTheUser = true;
        endConnection();
    }

    /**
     * Closes the socket of the client.
     */
    private void endConnection() {
        try {
            if (socket == null) return;

            isAlive = false;

            if(!socket.isClosed()) {
                socket.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void run() {
        try {
            socket = new Socket(serverAddress, SERVER_PORT);
            fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            connectionLostCallback.accept("Unable to establish connection, retry? y/other");
            return;
        }

        isAlive = true;
        stoppedByTheUser = false;

        try {
            while (isAlive) {
                Message serverMessage = readFromServer();
                if(stoppedByTheUser) return;
                callback.accept(serverMessage);
            }
        } catch (RuntimeException e) {
            if (socket.isClosed()) connectionLostCallback.accept("Lost connection to server, retry? y/other");
            else connectionLostCallback.accept(e.getMessage());
            isAlive = false;
        }
    }

    public boolean isConnected() { return isAlive; }

    public void setOnMessageReceivedCallback(Consumer<Message> callback) {
        this.callback = callback;
    }

    public void setOnConnectionLostCallback(Consumer<String> callback) {
        this.connectionLostCallback = callback;
    }

    /**
     * Starts the client.
     */
    public void start() {
        new Thread(this::run).start();
    }
}