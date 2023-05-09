package it.polimi.ingsw.client;

import it.polimi.ingsw.server.messages.Message;
import it.polimi.ingsw.util.observer.ClientObservable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client extends ClientObservable {

    private final Socket socket;
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;
    private String username;

    protected Message message;

    public Client(String address, int port){
        this.socket = new Socket();
        try {
            this.socket.connect(new InetSocketAddress(address,port));
            this.toServer = new ObjectOutputStream(socket.getOutputStream());
            this.fromServer = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readMessageFromServer() {
        new Thread(() -> {
            while(socket.isConnected())
            {
                try{
                    Message message = (Message) fromServer.readObject();
                    notifyObservers(obs->obs.update(message));
                }catch(Exception e){
                    endConnection();
                }
            }

        }).start();
    }

    public void sendMessageToServer(Message message) {
        try {
            toServer.writeObject(message);
            toServer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            endConnection();
        }
    }

    public void endConnection() {
        try {
            if(!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Socket getSocket() {
        return socket;
    }
}

