package it.polimi.ingsw.server;

import it.polimi.ingsw.server.messages.MessageType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for Server configuration methods and connection event handlers")
class ServerConfigurationTest {

    //Attributes for debugging console output
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();


    @Test
    @DisplayName("Should throw an exception if a middleware is set to a nonexistent callback")
    void invalidMiddlewareTest(){
        Server s = new Server(54321, MessageType.JSON, true);
        s.stop();
        assertThrows(RuntimeException.class, ()->s.setMiddleware("INVALID", (a, b, c)->{}));
    }

    @Test
    @DisplayName("Should print a startup message to console when started")
    void startupMessageTest(){
        System.setOut(new PrintStream(outputStreamCaptor));
        Server s = new Server(54321, MessageType.JSON);
        new Thread(this::startDummyClient).start();

        s.onConnection((c)->{
            s.stop(); //When someone connects, stop the server
        });

        s.start();
        assertTrue(outputStreamCaptor.toString().contains("Server started on port 54321 at:"));
        System.setOut(standardOut);
    }

    @Test
    @DisplayName("Should print a message to console when someone connects and disconnects")
    void connectionMessageTest(){
        System.setOut(new PrintStream(outputStreamCaptor));
        Server s = new Server(54321, MessageType.JSON, true);

        new Thread(this::startDummyClient).start();


        s.onConnection((c)->{
            System.out.println("connection open");
        });

        s.onConnectionClosed((c)->{
            System.out.println("connection closed");
            s.stop();
        });

        s.start();

        String consoleOutput = outputStreamCaptor.toString();
        consoleOutput = consoleOutput.replaceAll("\\r\\n?", "\n"); //Normalize line separators

        assertEquals("connection open\nconnection closed\n", consoleOutput);

        System.setOut(standardOut);
    }


    // ==================== DUMMY CLIENT ====================
    private void startDummyClient(){
        Socket s = null;
        try {
            s = new Socket("localhost", 54321);
            s.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}