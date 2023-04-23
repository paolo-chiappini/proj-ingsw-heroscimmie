package it.polimi.ingsw.server.messages;

import java.net.Socket;
import java.util.List;

/**
 * Message factory
 */
public class MessageProvider {

    private final MessageType messageType;
    private final Message.Builder builder;

    public MessageProvider(MessageType messageType) {
        this.messageType = messageType;
        this.builder = selectBuilder();
    }

    private Message.Builder selectBuilder(){
        Message.Builder instance = null;

        switch (messageType) {
            case JSON -> instance = new JsonMessage.JsonMessageBuilder();
            case SIMPLE -> instance = new SimpleMessage.SimpleMessageBuilder();
        }

        if(instance == null) throw new RuntimeException(messageType + " does not exist");

        return instance;
    }

    public Message getRequestInstance(Socket clientSocket, String clientData){
        return builder.setClientSocket(clientSocket)
                      .setData(clientData)
                      .build();
    }

    public Message getResponseInstance(Socket clientSocket, List<Socket> clientConnections, String clientData){
        return builder.setClientSocket(clientSocket)
                .setClientConnections(clientConnections)
                .setData(clientData)
                .build();
    }


}
