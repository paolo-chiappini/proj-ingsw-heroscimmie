package it.polimi.ingsw.server.messages;
import org.json.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * Message format that uses a simple JSON representation:
 * <p>
 * {
 *     "method" : "",
 *     "body"   : ""
 * }
 */
public class JsonMessage extends Message{
    private final String EMPTY_JSON_MESSAGE = "{\"method\":\"\", \"body\":\"\"}";
    private final JSONObject jsonData;

    public JsonMessage(Socket clientSocket, List<Socket> clientConnections, String data) {
        super(clientSocket, clientConnections, data);
        if(data.isEmpty()) data = EMPTY_JSON_MESSAGE;
        jsonData = new JSONObject(data);
    }

    @Override
    public String getMethod() {
        return  jsonData.getString("method");
    }

    @Override
    public String getBody() {
        return jsonData.getString("body");
    }

    @Override
    public void send() {
        writer.println(jsonData);
    }

    @Override
    public void send(String message) {
        writer.println(message);
    }

    @Override
    public void sendToAll() {
        for(var client : clientConnections){
            try {
                var out = new PrintWriter(client.getOutputStream(), true);
                out.println(jsonData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void setMethod(String method) {
        jsonData.put("method", method);
    }

    @Override
    public void setBody(String body) {
        jsonData.put("body", body);
    }

}
