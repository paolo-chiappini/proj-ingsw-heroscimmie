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
class JsonMessage extends Message{
    private JSONObject jsonData;

    private JsonMessage(Socket clientSocket, List<Socket> clientConnections, JSONObject data) {
        super(clientSocket, clientConnections);
        this.jsonData = data;

        String EMPTY_JSON_MESSAGE = "{\"method\":\"\", \"body\":\"\"}";
        if(jsonData.isEmpty())
            jsonData = new JSONObject(EMPTY_JSON_MESSAGE);
    }

    @Override
    public String getMethod() {
        return  jsonData.getString("method");
    }

    @Override
    public String getBody() {
        return jsonData.get("body").toString();
    }

    @Override
    public void send() {
        clientWriter.println(jsonData);
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
        JSONObject jsonObject;
        JSONArray jsonArray;
        try {
            jsonObject = new JSONObject(body);
            jsonData.put("body", jsonObject);
        }catch (JSONException notObjectException){
            try {
                jsonArray = new JSONArray(body);
                jsonData.put("body", jsonArray);
            }catch (JSONException notArrayException){
                jsonData.put("body", body);
            }
        }
    }

    public static class JsonMessageBuilder extends MessageBuilder {

        @Override
        public Message build() {
            if(this.data.isEmpty()) this.data = "{}";
            return new JsonMessage(clientSocket, clientConnections, new JSONObject(this.data));
        }
    }

}
