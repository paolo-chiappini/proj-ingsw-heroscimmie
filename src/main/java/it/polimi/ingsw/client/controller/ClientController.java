package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.virtualModel.ClientBoard;
import it.polimi.ingsw.client.virtualModel.ClientCommonGoalCard;
import it.polimi.ingsw.client.virtualModel.ClientPlayer;
import it.polimi.ingsw.client.virtualModel.ClientTurnState;
import it.polimi.ingsw.server.messages.Message;
import it.polimi.ingsw.util.observer.ViewListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class ClientController implements ViewListener {
    private Client client;
    private String myUsername;
    private ClientBoard board;
    private List<ClientPlayer> players;
    private List<ClientCommonGoalCard> commonGoalCards;
    private ClientTurnState turnState;
    private final View view;
    private int row1, row2, col1, col2, first, second, third;


    public ClientController(View view, String serverAddress) {
        this.view = view;

        try {
            client = new Client(serverAddress);
        } catch (RuntimeException re) {
            System.out.println("Unable to establish connection to server, exiting");
            return;
        }

        view.addListener(this);

        client.setOnMessageReceivedCallback(this::onMessageReceived);
        client.start();

        // Start view on a different tread
        new Thread(view).start();
    }


    public void onMessageReceived(Message message) {
        String method = message.getMethod();
        switch (method) {
            case "START" -> onGameStart(message);
            case "UPDATE" -> update(message);
            case "CHAT" -> onChatMessageReceived(message);
            case "LIST" -> onListReceived(message);
            case "LEFT" ->
                    System.out.println(new JSONObject(message.getBody()).getString("username") + " left the game");
            case "OK", "ERR" -> System.out.println(new JSONObject(message.getBody()).getString("msg"));
            default -> {
            }
        }
    }


    public void onListReceived(Message message) {
        JSONObject body = new JSONObject(message.getBody());
        JSONArray files = body.getJSONArray("files");
        if (files.isEmpty()) System.out.println("There are no files");
        else {
            System.out.println("List of saved files");
            for (int i = 0; i < files.length(); i++)
                System.out.println(files.get(i).toString());
        }
    }


    public void onChatMessageReceived(Message message) {
        JSONObject body = new JSONObject(message.getBody());
        String chatMessage = body.getString("message");
        String sender = body.getString("username");
        String recipient = null;
        if (body.has("recipient")) recipient = body.getString("recipient");

        if (recipient != null && (recipient.equals(myUsername) || sender.equals(myUsername)))
            view.addMessage(chatMessage, sender, true);
        else if (recipient == null)
            view.addMessage(chatMessage, sender, false);

        view.finalizeUpdate();
    }


    public void update(Message message) {
        JSONObject body = new JSONObject(message.getBody());

        JSONObject serialized = body.getJSONObject("serialized");
        JSONArray jsonPlayers = serialized.getJSONArray("players");
        JSONArray jsonCommonGoals = serialized.getJSONArray("common_goals");

        board.updateBoard(serialized.toString());

        for (int i = 0; i < jsonPlayers.length(); i++) {
            ClientPlayer player = players.get(i);
            player.updatePlayer(jsonPlayers.getJSONObject(i).toString());
        }

        for (int i = 0; i < jsonCommonGoals.length(); i++) {
            commonGoalCards.get(i).updatePoints(jsonCommonGoals.getJSONObject(i).toString());
        }

        view.finalizeUpdate();
    }

    private void handleGameEndAndReset() {
    }

    private void handleTurnChange(int turnIndex) {
        if (players.get(turnIndex).getUsername().equals(myUsername)) {
            return;
        }
    }

    public void onGameStart(Message message) {
        JSONObject body = new JSONObject(message.getBody());
        JSONObject serialized = body.getJSONObject("serialized");
        JSONArray jsonPlayers = serialized.getJSONArray("players");
        JSONArray jsonCommonGoals = serialized.getJSONArray("common_goals");

        this.board = new ClientBoard();
        this.players = new LinkedList<>();
        this.commonGoalCards = new LinkedList<>();
        this.turnState = new ClientTurnState();

        this.board.addListener(view);
        this.turnState.addListener(view);

        for (int i = 0; i < jsonPlayers.length(); i++) {
            JSONObject player = jsonPlayers.getJSONObject(i);
            this.players.add(new ClientPlayer(player.getString("username")));
            this.players.get(i).addListener(view);
            view.addPlayer(player.getString("username"), player.getInt("score"), player.getString("username").equals(myUsername));
            if (player.getString("username").equals(myUsername))
                this.players.get(i).updateId(player.toString());
        }

        for (int i = 0; i < jsonCommonGoals.length(); i++) {
            this.commonGoalCards.add(new ClientCommonGoalCard());
            this.commonGoalCards.get(i).addListener(view);
            commonGoalCards.get(i).updateId(jsonCommonGoals.getJSONObject(i).toString());
        }
        this.board.updateBoard(serialized.toString());

        update(message);
    }


    public void onJoinGame() {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        client.sendRequest("JOIN", body.toString());
    }


    public void onNewGame(int lobbySize) {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        body.put("lobby_size", lobbySize);
        client.sendRequest("NEW", body.toString());
    }


    public void onListSavedGames() {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        client.sendRequest("LIST", body.toString());
    }


    public void onLoadSavedGame(String saveName) {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        body.put("save_name", saveName);
        client.sendRequest("LOAD", body.toString());
    }


    public void onSaveCurrentGame() {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        client.sendRequest("SAVE", body.toString());
    }


    public void onQuitGame() {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        client.sendRequest("QUIT", body.toString());
    }


    public void onChooseUsername(String username) {
        System.out.println("updated username : " + username);
        this.myUsername = username;
    }

    public void onChooseColumnOfBookshelf(int numberOfColumn) {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        body.put("row1", row1);
        body.put("row2", row2);
        body.put("col1", col1);
        body.put("col2", col2);
        body.put("first_tile", first);
        body.put("second_tile", second);
        body.put("third_tile", third);
        body.put("column", numberOfColumn);
        System.out.println("sending message");
        client.sendRequest("DROP", body.toString());
    }


    public void onChooseTilesOrder(int first, int second, int third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public void onChooseTilesOnBoard(int row1, int col1, int row2, int col2) {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        body.put("row1", row1);
        body.put("row2", row2);
        body.put("col1", col1);
        body.put("col2", col2);
        this.row1 = row1;
        this.col1 = col1;
        this.row2 = row2;
        this.col2 = col2;
        System.out.println("sending message");
        client.sendRequest("PICK", body.toString());
    }


    public void onChatMessageSent(String message) {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        body.put("message", message);
        System.out.println("sending message");
        client.sendRequest("CHAT", body.toString());
    }


    public void onChatWhisperSent(String message, String recipient) {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        body.put("message", message);
        body.put("recipient", recipient);
        System.out.println("sending whisper");
        client.sendRequest("CHAT", body.toString());
    }

    public void onEndOfTurn() {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        client.sendRequest("NEXT", body.toString());
    }
}
