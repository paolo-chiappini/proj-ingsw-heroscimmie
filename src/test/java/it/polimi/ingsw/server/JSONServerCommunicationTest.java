package it.polimi.ingsw.server;

import it.polimi.ingsw.server.messages.Message;
import it.polimi.ingsw.server.messages.MessageProvider;
import it.polimi.ingsw.server.messages.MessageType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

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

        syncLatch.await(); // Wait for server initialization...

        Socket server =  new Socket("localhost", 33333); // ...and then connect

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

        syncLatch.await(); // Wait for server initialization...

        Socket server =  new Socket("localhost", 33333); // ...and then connect

        Message hello = makeRequest(server, "HELO", "");
        Message cap = makeRequest(server, "CAP", "maximize me!");
        Message min = makeRequest(server, "MIN", "MINIMIZE ME!");

        server.close();

        assertAll(
                ()->assertEquals("GLOBAL MIDDLEWARE", hello.getMethod()),
                ()->assertEquals("Hello, World!", hello.getBody()),
                ()->assertEquals("SPECIFIC MIDDLEWARE", cap.getMethod()),
                ()->assertEquals("MAXIMIZE ME!", cap.getBody()),
                ()->assertEquals("SPECIFIC MIDDLEWARE", min.getMethod()),
                ()->assertEquals("minimize me!", min.getBody())
        );
    }


    // ==================== SERVER CONFIGURATIONS ====================

    private void simpleEchoServer(){
        Server s = new Server(33333, MessageType.JSON, true);

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
        Server s = new Server(33333, MessageType.JSON, true);

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
            response.setBody("Hello, World!");
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

    // ==================== REQUEST UTILS ====================
    private Message makeRequest(Socket server, String method, String body){
        try {
            var reader = new BufferedReader(new InputStreamReader(server.getInputStream()));

            var factory = new MessageProvider(MessageType.JSON);

            Message request = factory.getEmptyInstance(server);

            request.setMethod(method);
            request.setBody(body);

            request.send();

            return factory.getInstanceForIncomingRequest(server, reader.readLine());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}