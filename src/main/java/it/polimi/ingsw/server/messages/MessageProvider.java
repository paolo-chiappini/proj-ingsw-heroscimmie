package it.polimi.ingsw.server.messages;

import java.net.Socket;
import java.util.List;
//TODO Aliases for Methods
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
     * Builds a Message instance initialized for containing an incoming client request
     * @param clientSocket socket of the client that made the request
     * @param clientData raw data from the client
     * @return A pre-initialized instance of Message
     */
    public Message getInstanceForIncomingRequest(Socket clientSocket, String clientData){
        return builder.setClientSocket(clientSocket)
                      .setData(clientData)
                      .build();
    }

    /**
     * Builds a Message instance initialized for containing a server response
     * @param clientSocket socket of the response recipient
     * @param clientConnections socket list to use when broadcasting the response
     * @return A pre-initialized instance of Message
     */
    public Message getInstanceForOutgoingResponse(Socket clientSocket, List<Socket> clientConnections){
        return builder.setClientSocket(clientSocket)
                .setClientConnections(clientConnections)
                .build();
    }

    /**
     * Builds a empty Message instance
     * @param serverSocket socket of the response recipient
     * @return A pre-initialized instance of Message
     */
    public Message getEmptyInstance(Socket serverSocket){
        return builder.setClientSocket(serverSocket)
                .build();
    }

}
