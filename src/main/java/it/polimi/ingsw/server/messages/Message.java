package it.polimi.ingsw.server.messages;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Message that is used for communication between a client and the Server
 */
public abstract class Message {
    protected final Socket clientSocket;
    protected final PrintWriter writer;
    protected List<Socket> clientConnections;

    public Message(Socket clientSocket, List<Socket> clientConnections, String data){
        this.clientSocket = clientSocket;
        this.clientConnections = new ArrayList<>();
        try {
            this.writer = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Returns the method of the message
     */
    public abstract String getMethod();
    /**
     * Sets the method of the message
     */
    public abstract void setMethod(String method);

    /**
     * Sets the body of the message
     */
    public abstract void setBody(String body);

    /**
     * Returns the body of the message
     */
    public abstract String getBody();

    /**
     * Sends the formatted message to the client socket associated with the Message object
     */
    public abstract void send();


    /**
     * <b>
     *     This method should only be used for testing, if you need to
     *     send a message, use send().
     * </b>
     * <p>
     * Sends a raw string of characters to the client socket associated with the Message object
     * @param message String of characters to send
     */
    public abstract void send(String message);

    /**
     * Broadcasts the formatted message to every socket associated with the Message object
     */
    public abstract void sendToAll();

    /**
     * <b>
     *     This method should only be used for testing, if you need to send a message, use sendToAll().
     * </b>
     * <p>
     * Broadcasts a raw string of characters to every socket associated with the Message object
     * @param message String of characters to send
     */
    public void sendToAll(String message) {
        for(var client : clientConnections){
            try {
                var writer = new PrintWriter(client.getOutputStream(), true);
                writer.println(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
