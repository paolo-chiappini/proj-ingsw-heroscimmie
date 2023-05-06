package it.polimi.ingsw.server;

import it.polimi.ingsw.server.messages.Message;
import it.polimi.ingsw.server.messages.MessageProvider;
import it.polimi.ingsw.server.messages.MessageType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.Duration;
import java.util.concurrent.*;
import org.junit.rules.Timeout;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/*
    All tests are from the client's point of view:

    Test(){
        setUpServer();
        waitForServerToInitialize();
        connectToServer();
        response = makeRequest("method", "body");
        assertEquals(expectedResponse, response);
    }
*/

@DisplayName("Tests for Server-side Network functions (callbacks and whatnot) using JSON messages")
class JSONServerCommunicationTest {

    final CountDownLatch syncLatch = new CountDownLatch(1); //Used for synchronization

    @Test
    @DisplayName("An ECHO request should be handled correctly")
    void echoTest() throws IOException, InterruptedException{
        new Thread(this::simpleEchoServer).start(); // Start the server

        var timeout = !syncLatch.await(4, TimeUnit.SECONDS); // Wait for server initialization...
        if(timeout) throw new RuntimeException("Server timed out");

        Socket server =  new Socket("localhost", 49152); // ...and then connect

        Message response = makeRequest(server, "ECHO", "Please repeat");

        server.close();

        String rcvdMethod = response.getMethod();
        String rcvdBody = response.getBody();

        assertAll(
                ()->assertEquals("ECHO", rcvdMethod),
                ()->assertEquals("Please repeat", rcvdBody)
        );
    }

    @Test
    @DisplayName("The correct middleware should be executed")
    void middlewareTest() throws IOException, InterruptedException{
        new Thread(this::middlewareServer).start(); // Start the server

        var timeout = !syncLatch.await(4, TimeUnit.SECONDS); // Wait for server initialization...
        if(timeout) throw new RuntimeException("Server timed out");

        Socket server =  new Socket("localhost", 34567); // ...and then connect

        Message hello = makeRequest(server, "HELO", "");
        Message cap = makeRequest(server, "CAP", "maximize me!");
        Message min = makeRequest(server, "MIN", "MINIMIZE ME!");

        server.close();

        assertAll(
                ()->assertEquals("GLOBAL MIDDLEWARE", hello.getMethod()),
                ()->assertEquals("{\"from\":\"Server\",\"message\":\"Hello, World!\"}", hello.getBody()),
                ()->assertEquals("SPECIFIC MIDDLEWARE", cap.getMethod()),
                ()->assertEquals("MAXIMIZE ME!", cap.getBody()),
                ()->assertEquals("SPECIFIC MIDDLEWARE", min.getMethod()),
                ()->assertEquals("minimize me!", min.getBody())
        );
    }

    @Test
    @DisplayName("An ECHO request with JSONObject body should be handled correctly")
    void jsonObjectEchoTest() throws IOException, InterruptedException{
        new Thread(this::simpleEchoServer).start(); // Start the server

        var timeout = !syncLatch.await(4, TimeUnit.SECONDS); // Wait for server initialization...
        if(timeout) throw new RuntimeException("Server timed out");

        Socket server =  new Socket("localhost", 49152); // ...and then connect

        JSONObject body = new JSONObject();
        body.put("name", "Cristina");
        body.put("surname", "D'Avena");

        JSONArray songs = new JSONArray();

        JSONObject s1 = new JSONObject();
        s1.put("title", "Occhi di gatto");
        s1.put("date", 1985);
        songs.put(s1);

        JSONObject s2 = new JSONObject();
        s2.put("title", "Kiss me Licia");
        s2.put("date", 1983);
        songs.put(s2);

        body.put("songs", songs);

        Message response = makeRequest(server, "ECHO", body.toString());

        server.close();

        JSONObject receivedJSON = new JSONObject();
        receivedJSON.put("method", response.getMethod());

        receivedJSON.put("body", new JSONObject(response.getBody()));


        JSONObject expectedJSON = new JSONObject("""
                {
                "method":"ECHO",
                "body":{
                    "name": "Cristina",
                    "surname": "D'Avena",
                    "songs": [
                        {
                            "title": "Occhi di gatto",
                            "date": 1985
                        },
                        {
                            "title": "Kiss me Licia",
                            "date": 1983
                        }
                    ]
                  }
                }
                """);

        assertEquals(expectedJSON.toString(), receivedJSON.toString());
    }

    @Test
    @DisplayName("An ECHO request with JSONArray body should be handled correctly")
    void jsonArrayEchoTest() throws IOException, InterruptedException{
        new Thread(this::simpleEchoServer).start(); // Start the server

        var timeout = !syncLatch.await(4, TimeUnit.SECONDS); // Wait for server initialization...
        if(timeout) throw new RuntimeException("Server timed out");

        Socket server =  new Socket("localhost", 49152); // ...and then connect

        JSONArray songs = new JSONArray();

        JSONObject s1 = new JSONObject();
        s1.put("title", "Occhi di gatto");
        s1.put("date", 1985);
        songs.put(s1);

        JSONObject s2 = new JSONObject();
        s2.put("title", "Kiss me Licia");
        s2.put("date", 1983);
        songs.put(s2);


        Message response = makeRequest(server, "ECHO", songs.toString());

        server.close();

        String rcvdMethod = response.getMethod();
        String rcvdBody = response.getBody();

        String expectedString ="[{\"date\":1985,\"title\":\"Occhi di gatto\"},{\"date\":1983,\"title\":\"Kiss me Licia\"}]";

        assertAll(
                ()->assertEquals("ECHO", rcvdMethod),
                ()->assertEquals(expectedString, rcvdBody)
        );
    }


    // ==================== SERVER CONFIGURATIONS ====================

    private void simpleEchoServer(){
        Server s = new Server(49152, MessageType.JSON, true);

        syncLatch.countDown(); //👌 Ready for connections

        s.onConnectionClosed((socket)->{
            s.stop();
        });

        s.setCallback("ECHO", (request, response) -> {
            response = request;
            response.send();
        });

        s.start();
    }

    private void middlewareServer(){
        Server s = new Server(34567, MessageType.JSON, true);

        syncLatch.countDown(); //👌 Ready for connections

        s.onConnectionClosed((socket)->{
            s.stop();
        });

        s.setCallback("CAP", (request, response) -> {
            response.setBody(response.getBody().toUpperCase());
            response.send();
        });

        s.setCallback("MIN", (request, response) -> {
            response.setBody(response.getBody().toLowerCase());
            response.send();
        });

        s.setCallback("HELO", (request, response) -> {
            JSONObject body = new JSONObject();
            body.put("from", "Server");
            body.put("message", "Hello, World!");
            response.setBody(body.toString());
            response.send();
        });

        s.setGlobalMiddleware((req, res, next)->{
            res.setMethod("GLOBAL MIDDLEWARE");
            next.call(req, res);
        });

        s.setMiddleware(new String[]{"CAP", "MIN"}, (req, res, next)->{
            res.setMethod("SPECIFIC MIDDLEWARE");
            next.call(req, res);
        });

        s.start();
    }

    // ==================== UTILS ====================
    private Message makeRequest(Socket server, String method, String body) {
        try {
            var reader = new BufferedReader(new InputStreamReader(server.getInputStream()));

            var factory = new MessageProvider(MessageType.JSON);

            Message request = factory.getEmptyInstance(server);

            request.setMethod(method);
            request.setBody(body);
            request.send();

            String response = reader.readLine();

            return factory.getInstanceForIncomingRequest(server, response);

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}