package it.polimi.ingsw.server.messages;

import java.net.Socket;
import java.util.List;

/**
 * Message factory.
 */
public class MessageProvider {

    private final MessageType messageType;
    private final Message.MessageBuilder builder;

    public MessageProvider(MessageType messageType) {
        this.messageType = messageType;
        this.builder = selectBuilder();
    }

    private Message.MessageBuilder selectBuilder(){
        Message.MessageBuilder instance = null;

        switch (messageType) {
            case JSON -> instance = new JsonMessage.JsonMessageBuilder();
            case SIMPLE -> instance = new SimpleMessage.SimpleMessageBuilder();
        }

        if(instance == null) throw new RuntimeException(messageType + " does not exist");

        return instance;
    }

    /**
     * Builds a Message instance initialized for containing a request type message
     * @param clientSocket socket of the client that made the request
     * @param clientData raw data from the client
     * @return A pre-initialized instance of Message
     */
    public Message getInstanceForRequest(Socket clientSocket, String clientData){
        return builder.setClientSocket(clientSocket)
                      .setData(clientData)
                      .build();
    }

    /**
     * Builds a Message instance initialized for containing a response type message
     * @param clientSocket socket of the response recipient
     * @param clientConnections socket list to use when broadcasting the response
     * @return A pre-initialized instance of Message
     */
    public Message getInstanceForResponse(Socket clientSocket, List<Socket> clientConnections){
        return builder.setClientSocket(clientSocket)
                .setClientConnections(clientConnections)
                .build();
    }

    /**
     * Builds a Message instance initialized for containing a response type message
     * @param clientSocket socket of the response recipient
     * @return A pre-initialized instance of Message
     */
    public Message getInstanceForResponse(Socket clientSocket){
        return builder.setClientSocket(clientSocket)
                .build();
    }
}
