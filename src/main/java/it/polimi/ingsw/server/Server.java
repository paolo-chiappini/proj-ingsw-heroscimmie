package it.polimi.ingsw.server;

import it.polimi.ingsw.server.messages.Message;
import it.polimi.ingsw.server.messages.MessageType;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.function.Consumer;

public class Server {
    private final ServerSocket serverSocket;
    private final HashMap<String, Callback<Message>> callbacks;
    private final HashMap<String, Middleware<Message>> middlewares;
    private final MessageType messageType;
    private final List<Socket> clientConnections;
    private Consumer<Socket> onConnection;
    private Consumer<Socket> onConnectionLost;


    private Consumer<Socket> onConnectionClosed;

    private Middleware<Message> globalMiddleware;
    private Callback<Message> defaultCallback;

    public Server(int port, MessageType messageType) {
        this.messageType = messageType;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        clientConnections = new ArrayList<>();
        callbacks = new HashMap<>();
        middlewares = new HashMap<>();

        globalMiddleware = (req, res, specificMiddleware) -> specificMiddleware.apply(req, res);
    }

    public void setDefaultCallback(Callback<Message> defaultCallback) {
        this.defaultCallback = defaultCallback;
    }

    public void setCallback(String method, Callback<Message> callback){
        if(callbacks.containsKey(method)){
            throw new RuntimeException("Request method \"" + method + "\" is already bound to a callback");
        }
        callbacks.put(method, callback);
    }

    public Callback<Message> getCallback(String method){
        var callback = callbacks.get(method);

        if(callback == null) callback = defaultCallback;

        return callback;
    }

    public void start(){
        try{
            System.out.printf("Server started on port %d at:\n", serverSocket.getLocalPort());
            printNetworkInterfaces();

            while (true) { //Server should keep running indefinitely
                Socket clientSocket = serverSocket.accept();
                clientConnections.add(clientSocket);
                var clientHandler = new ClientHandler(clientSocket, this, messageType);
                clientHandler.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setGlobalMiddleware(Middleware<Message> function) {
        this.globalMiddleware = function;
    }

    public Middleware<Message> getGlobalMiddleware() {
        return globalMiddleware;
    }

    public void setMiddleware(String method, Middleware<Message> function) {
        if(!callbacks.containsKey(method)){
            throw new RuntimeException("method \"%s\" not bound to a callback".formatted(method));
        }

        middlewares.put(method, function);
    }

    public void setMiddleware(String[] methods, Middleware<Message> function) {
        for(var method : methods){
            if(!callbacks.containsKey(method)){
                throw new RuntimeException("method \"%s\" not bound to a callback".formatted(method));
            }
            middlewares.put(method, function);
        }
    }

    public Middleware<Message> getMiddleware(String method) {
        var middleware = middlewares.get(method);

        if (middleware == null){
            middleware = (req, res, callback) -> callback.apply(req, res);
        }

        return middleware;
    }

    public void onConnection(Consumer<Socket> function) {
        onConnection = function;
    }

    public void onConnectionLost(Consumer<Socket> function) {
        onConnectionLost = function;
    }

    public void onConnectionClosed(Consumer<Socket> onConnectionClosed) {
        this.onConnectionClosed = onConnectionClosed;
    }

    public Consumer<Socket> getActionOnConnectionClosed() {
        return onConnectionClosed;
    }

    public Consumer<Socket> getActionOnConnection() {
        return onConnection;
    }

    public Consumer<Socket> getActionOnConnectionLost() {
        return onConnectionLost;
    }

    public List<Socket> getConnections() {
        return new ArrayList<>(this.clientConnections);
    }
    public void closeConnection(Socket clientSocket) {
        clientConnections.remove(clientSocket);
    }

    private void printNetworkInterfaces() throws SocketException {
        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        ArrayList<NetworkInterface> networkInterfaces = Collections.list(e);

        var filteredNetworkInterfaces = networkInterfaces.stream()
                .filter((el)-> !el.getDisplayName().toLowerCase().contains("virtual"))
                .toList();

        for(var netInt : filteredNetworkInterfaces){
            var addresses = Collections.list(netInt.getInetAddresses());
            for(var addr : addresses){
                if(addr.isLoopbackAddress() || addr instanceof Inet6Address) continue;

                System.out.println(netInt.getDisplayName()+" "+ addr.getHostAddress());
            }
        }
    }
}
