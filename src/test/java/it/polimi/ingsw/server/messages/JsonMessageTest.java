package it.polimi.ingsw.server.messages;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.concurrent.CountDownLatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


/*
    All tests are from the receiver's point of view:

    Test(){
        startSenderThread();
        waitForSenderToInitialize();
        connectToSender();
        data = receiveDataFromSender();
        assertEquals("expectedData", data);
    }
*/

@DisplayName("Tests for sending and receiving JSON messages")
class JsonMessageTest {

    final CountDownLatch syncLatch = new CountDownLatch(1); //Used for synchronization

    // ==================== SINGLE MESSAGE TESTS ====================
    @Test
    @DisplayName("Body and method should be sent and received correctly")
    void singleMessageSendTest() throws IOException, InterruptedException {
        new Thread(this::simpleSenderTask).start(); //Starts the sender

        syncLatch.await(); //Waits for sender socket initialization...

        Socket receiver =  new Socket("localhost", 55555); //... and then connects

        var reader = new BufferedReader(new InputStreamReader(receiver.getInputStream()));
        String senderData = reader.readLine();

        var factory = new MessageProvider(MessageType.JSON);
        Message msg = factory.getInstanceForIncomingRequest(receiver, senderData);

        String rcvdMethod = msg.getMethod();
        String rcvdBody = msg.getBody();

        assertAll(
                ()->assertEquals("TEST", rcvdMethod),
                ()->assertEquals("This is a test message", rcvdBody)
        );
    }

    @Test
    @DisplayName("String should be sent and received correctly")
    void simpleSingleMessageSendTest() throws IOException, InterruptedException {
        new Thread(this::simpleSenderTask).start();
        syncLatch.await();

        Socket receiver =  new Socket("localhost", 55555);

        var reader = new BufferedReader(new InputStreamReader(receiver.getInputStream()));
        String senderData = reader.readLine();

        var expected = new JSONObject();
        expected.put("method", "TEST");
        expected.put("body", "This is a test message");

        assertEquals(expected.toString(), senderData);
    }

    // ==================== BROADCAST TESTS ====================

    @Test
    @DisplayName("The same method and body should be received by every connected socket")
    void broadcastSendTest() throws IOException, InterruptedException {
        new Thread(this::broadcastSenderTask).start();
        syncLatch.await();

        List<Socket> connections = new ArrayList<>();
        var factory = new MessageProvider(MessageType.JSON);

        //Creates 5 connections to the sender
        for (int i = 0; i < 5; i++){
            connections.add(new Socket("localhost", 44444));
        }

        List<Message> messages = new ArrayList<>();

        //For every connection, save its input buffer
        for (Socket s : connections){
            var reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            messages.add(factory.getInstanceForIncomingRequest(s, reader.readLine()));
        }


        assertTrue(messages.stream().allMatch((m)->{
            return m.getMethod().equals("TEST") &&
                   m.getBody().equals("This is a test message");
        }));
    }

    @Test
    @DisplayName("String should be broadcast to every connected socket")
    void simpleBroadcastSendTest() throws IOException, InterruptedException {
        new Thread(this::broadcastSenderTask).start();
        syncLatch.await();

        List<Socket> connections = new ArrayList<>();

        for (int i = 0; i < 5; i++){
            connections.add(new Socket("localhost", 44444));
        }

        List<String> messages = new ArrayList<>();

        for (Socket s : connections){
            var reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            messages.add(reader.readLine());
        }

        var expected = new JSONObject();
        expected.put("method", "TEST");
        expected.put("body", "This is a test message");

        assertTrue(messages.stream().allMatch((m)->m.equals(expected.toString())));
    }


    // ==================== SENDER THREAD TASKS ====================

    private void simpleSenderTask(){
        try {
            ServerSocket sender = new ServerSocket(55555);
            syncLatch.countDown(); //👌 Ready for connections
            Socket socket = sender.accept();

            var factory = new MessageProvider(MessageType.JSON);
            Message msg = factory.getEmptyInstance(socket);

            msg.setMethod("TEST");
            msg.setBody("This is a test message");
            msg.send();


            sender.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void broadcastSenderTask(){
        try {
            ServerSocket sender = new ServerSocket(44444);
            List<Socket> connections = new ArrayList<>();

            syncLatch.countDown(); //👌 Ready for connections

            for (int i = 0; i < 5; i++){
                connections.add(sender.accept());
            }

            var factory = new MessageProvider(MessageType.JSON);
            Message msg = factory.getInstanceForOutgoingResponse(connections.get(0), connections);

            msg.setMethod("TEST");
            msg.setBody("This is a test message");
            msg.sendToAll();


            sender.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}