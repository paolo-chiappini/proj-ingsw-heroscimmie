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

    private final Socket socket;
    private final BufferedReader fromServer;

    protected Message message;
    private final MessageProvider messageProvider = new MessageProvider(MessageType.JSON);
    private Consumer<Message> callback;
    private static final int SERVER_PORT=49152;

    public Client(String address){
        try {
            this.socket = new Socket(address, SERVER_PORT);
            fromServer = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // default callback
        callback = (msg) -> {};
    }

    public void sendRequest(String method, String body) {
        Message request = messageProvider.getEmptyInstance(socket);
        request.setMethod(method);
        request.setBody(body);
        request.send();
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
            if(!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void run() {
        while (true) {
            Message serverMessage = readFromServer();
            callback.accept(serverMessage);
        }
    }

    public void setOnMessageReceivedCallback(Consumer<Message> callback) {
        this.callback = callback;
    }

    public void start() {
        new Thread(this::run).start();
    }
}