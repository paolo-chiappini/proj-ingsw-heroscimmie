package it.polimi.ingsw.server.messages;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * <b>
 *     This class should only be used for testing
 * </b>
 * <p>
 * Simple message format: method§body
 */
class SimpleMessage extends Message{

    private String rawMessage;

    private SimpleMessage(Socket socketWriter, List<Socket> clientConnections, String data) {
        super(socketWriter, clientConnections);
        this.rawMessage = data;
    }


    @Override
    public String getMethod() {
        var method = rawMessage.split("§");
        if(method.length == 0) return "";
        return method[0];
    }

    @Override
    public String getBody() {
        var body = rawMessage.split("§");
        if(body.length == 0) return "";
        return body[1];
    }

    @Override
    public void send() {
        clientWriter.println(rawMessage);
    }

    @Override
    public void send(String message) {
        clientWriter.println(message);
    }

    @Override
    public void sendToAll() {
        for(var client : clientConnections){
            try {
                var out = new PrintWriter(client.getOutputStream(), true);
                out.println(rawMessage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String toString(){
        return rawMessage;
    }

    @Override
    public void setMethod(String method) {
        rawMessage = method+'§'+rawMessage.split("§")[1];
    }

    @Override
    public void setBody(String body) {
        rawMessage = rawMessage.split("§")[0]+'§'+body;
    }


    public static class SimpleMessageBuilder extends MessageBuilder {
        @Override
        public Message build() {
            return new SimpleMessage(clientSocket, clientConnections, data);
        }
    }
}
