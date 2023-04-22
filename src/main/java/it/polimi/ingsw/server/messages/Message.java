package it.polimi.ingsw.server.messages;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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


    public abstract String getMethod();
    public abstract void setMethod(String method);
    public abstract String getBody();
    public abstract void setBody(String body);
    public abstract void send();
    public abstract void send(String message);
    public void sendToAll(String s) {
        for(var client : clientConnections){
            try {
                var writer = new PrintWriter(client.getOutputStream(), true);
                writer.println(s);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public abstract void sendToAll();
}
