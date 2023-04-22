package it.polimi.ingsw.server.messages;

import java.net.Socket;
import java.util.List;

/**
 * Message factory
 */
public class MessageProvider {

    public static Message getInstanceOf(MessageType messageType, Socket clientSocket, List<Socket> clientConnections, String inputData){
        Message instance = null;

        switch (messageType) {
            case JSON -> instance = new JsonMessage(clientSocket, clientConnections, inputData);
            case SIMPLE -> instance = new SimpleMessage(clientSocket, clientConnections, inputData);
        }

        if(instance == null) throw new RuntimeException(messageType + " does not exist");

        return instance;
    }

    public static Message getInstanceOf(MessageType messageType, Socket clientSocket, List<Socket> clientConnections){
        return getInstanceOf(messageType, clientSocket, clientConnections, "");
    }

    public static Message getInstanceOf(MessageType messageType, Socket clientSocket, String inputData){
        return getInstanceOf(messageType, clientSocket, null, inputData);
    }


    public static Message getInstanceOf(MessageType messageType, Socket socket) {
        return getInstanceOf(messageType, socket, null, "");
    }
}
