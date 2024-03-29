package it.polimi.ingsw.server;

import it.polimi.ingsw.server.messages.MessageType;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.function.Consumer;

/**
 *  Handles client requests and connection events.
 */
public class Server {
    private ServerSocket serverSocket;
    private final HashMap<String, Callback> callbacks;
    private final HashMap<String, Middleware> middlewares;
    private final MessageType messageType;
    private boolean suppressStartupMessage;
    private boolean running;
    private final List<Socket> clientConnections;

    private Consumer<Socket> onConnection;
    private Consumer<Socket> onConnectionLost;
    private Consumer<Socket> onConnectionClosed;
    private Middleware globalMiddleware;
    private Callback defaultCallback;

    /**
     * Acquires network resources and creates a Server instance
     * @param port TCP port on which the server is listening for connections
     * @param messageType Format used by the server to send and receive messages
     */
    public Server(int port, MessageType messageType) {
        this.messageType = messageType;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Can't start server, port already in use");
            System.exit(1);
        }

        clientConnections = new ArrayList<>();
        callbacks = new HashMap<>();
        middlewares = new HashMap<>();

        // Default values
        suppressStartupMessage = false;
        running = false;
        defaultCallback = (req, res) -> res.send();
        globalMiddleware = (req, res, specificMiddleware) -> specificMiddleware.call(req, res);
        onConnection = (s)->{};
        onConnectionLost = (s)->{};
        onConnectionClosed = (s)->{};
    }

    /**
     * Acquires network resources and creates a Server instance
     * @param port TCP port on which the server is listening for connections
     * @param messageType Format used by the server to send and receive messages
     * @param suppressStartupMessage Do not show startup message
     */
    public Server(int port, MessageType messageType, boolean suppressStartupMessage) {
        this(port, messageType);
        this.suppressStartupMessage = suppressStartupMessage;
    }

    /**
     * Starts the server. <b>This is a blocking function<b/>
     */
    public void start(){
        try{
            if(!suppressStartupMessage){
                System.out.printf("Server started on port %d at:\n", serverSocket.getLocalPort());
                printNetworkInterfaces();
            }

            running = true;

            while (running) { //Server accept infinite loop
                Socket clientSocket = serverSocket.accept();
                clientConnections.add(clientSocket);

                var clientHandler = new ClientHandler(clientSocket, this, messageType);
                clientHandler.start();
            }

        } catch (IOException e) {
            if(!running) return; //If the Server isn't running, then the exception is expected
            e.printStackTrace();
        }
    }

    /**
     * Releases all resources and stops the server
     */
    public void stop() {
        running = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // =========== CALLBACK METHODS ===========

    /**
     * Sets the default handler function.
     * @param defaultCallback It's called when a request isn't bound to any particular callback
     */
    public void setDefaultCallback(Callback defaultCallback) {
        this.defaultCallback = defaultCallback;
    }


    /**
     * Assigns a callback function to a particular request method
     * @param method Request's method
     * @param callback Callbacks function that handles the request
     */
    public void setCallback(String method, Callback callback){
        callbacks.put(method, callback);
    }

    /**
     * Returns the callback that handles the specified method. If there are none, it returns the default callback.
     * @param method Request's method
     */
    public Callback getCallback(String method){
        var callback = callbacks.get(method);

        if(callback == null) callback = defaultCallback;

        return callback;
    }

    // =========== MIDDLEWARE METHODS ===========

    /**
     * Sets the global middleware function.
     * @param function Middleware that is executed for every request
     */
    public void setGlobalMiddleware(Middleware function) {
        this.globalMiddleware = function;
    }

    /**
     * Returns the global middleware function that is executed for every request.
     */
    public Middleware getGlobalMiddleware() {
        return globalMiddleware;
    }

    /**
     * Sets a middleware function to a particular request's method
     * @param method Request's method
     * @param function Middleware function
     */
    public void setMiddleware(String method, Middleware function) {
        if(!callbacks.containsKey(method)){
            throw new RuntimeException("method \"%s\" not bound to a callback".formatted(method));
        }

        middlewares.put(method, function);
    }


    /**
     * Sets the global middleware function for every specified request's method
     * @param methods Array of request methods
     * @param function Middleware function
     */
    public void setMiddleware(String[] methods, Middleware function) {
        for(var method : methods){
            setMiddleware(method, function);
        }
    }

    /**
     * Returns the middleware function bound to a particular request method
     * @param method Request's method
     */
    public Middleware getMiddleware(String method) {
        var middleware = middlewares.get(method);

        if (middleware == null){
            middleware = (req, res, callback) -> callback.call(req, res);
        }

        return middleware;
    }


    // =========== EVENT METHODS ===========


    /**
     * Sets an event handler for when a client connects to the server
     * @param function An event handler function that takes the client socket as a parameter
     */
    public void onConnection(Consumer<Socket> function) {
        onConnection = function;
    }

    /**
     * Sets an event handler for when a connection to a client is lost
     * @param function An event handler function that takes the client socket as a parameter
     */
    public void onConnectionLost(Consumer<Socket> function) {
        onConnectionLost = function;
    }

    /**
     * Sets an event handler for when a connection is closed by a client
     * @param function An event handler function that takes the client socket as a parameter
     */
    public void onConnectionClosed(Consumer<Socket> function) {
        this.onConnectionClosed = function;
    }

    /**
     * Gets the event handler function for when a connection is closed by a client
     */
    public Consumer<Socket> getActionOnConnectionClosed() {
        return onConnectionClosed;
    }

    /**
     * Gets the event handler function for when a client connects to the server
     */
    public Consumer<Socket> getActionOnConnection() {
        return onConnection;
    }

    /**
     * Gets the event handler function for when a connection to a client is lost
     */
    public Consumer<Socket> getActionOnConnectionLost() {
        return onConnectionLost;
    }


    // =========== MISCELLANEOUS ===========

    /**
     * Returns a list of all client sockets currently connected to the server.
     */
    public List<Socket> getConnections() {
        return new ArrayList<>(this.clientConnections);
    }


    /**
     * Closes the connection to a client
     */
    public void closeConnection(Socket clientSocket) {
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        clientConnections.remove(clientSocket);
    }

    /**
     * Prints to the console all the valid private IP addresses of the server.
     */
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
