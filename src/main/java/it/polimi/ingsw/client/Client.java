package it.polimi.ingsw.client;

import it.polimi.ingsw.server.Callback;
import it.polimi.ingsw.server.messages.Message;
import it.polimi.ingsw.server.messages.MessageProvider;
import it.polimi.ingsw.server.messages.MessageType;
import it.polimi.ingsw.util.observer.ClientObservable;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends ClientObservable {

    private final Socket socket;
    private final BufferedReader fromServer;
    private String username;

    protected Message message;
    private final MessageProvider messageProvider = new MessageProvider(MessageType.JSON);
    private Consumer<Message> callback;

    public Client(String address, int port){
        try {
            this.socket = new Socket(address, port);
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
            throw new RuntimeException(e);
        }
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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Socket getSocket() {
        return socket;
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

