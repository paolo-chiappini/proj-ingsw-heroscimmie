package it.polimi.ingsw.client;

import it.polimi.ingsw.server.messages.Message;
import it.polimi.ingsw.server.messages.MessageProvider;
import it.polimi.ingsw.server.messages.MessageType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.function.Consumer;

public class Client {

    private Socket socket;
    private BufferedReader fromServer;

    protected Message message;
    private final MessageProvider messageProvider = new MessageProvider(MessageType.JSON);
    private Consumer<Message> callback;
    private Consumer<String> connectionLostCallback;
    private static final int SERVER_PORT=49152;
    private boolean isAlive;
    private String serverAddress;

    public Client(String address){
        serverAddress = address;

        // default callback
        callback = (msg) -> {};
    }

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

    public Message readFromServer() {
        try {
            return messageProvider.getInstanceForIncomingRequest(this.socket, fromServer.readLine());
        } catch (IOException e) {
            endConnection();
        }
        return null;
    }

    public void endConnection() {
        try {
            if (socket == null) return;
            if(!socket.isClosed()) {
                socket.close();
            }

            isAlive = false;
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

        try {
            while (isAlive) {
                Message serverMessage = readFromServer();
                if(!isAlive) return;
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

    public void start() {
        new Thread(this::run).start();
    }
}