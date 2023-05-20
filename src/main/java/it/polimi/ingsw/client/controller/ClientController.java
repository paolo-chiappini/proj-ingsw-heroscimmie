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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            view.showServerConnectionError();
            return;
        }

        view.addListener(this);

        client.setOnMessageReceivedCallback(this::onMessageReceived);
        client.start();

        // Start view on a different tread
        new Thread(view).start();
    }


    public void onMessageReceived(Message message) {
        if (message == null) {
            view.showServerConnectionError();
            return;
        }

        String method = message.getMethod();
        switch (method) {
            case "START" -> onGameStart(message);
            case "UPDATE" -> update(message);
            case "CHAT" -> onChatMessageReceived(message);
            case "LIST" -> onListReceived(message);
            case "LEFT" -> onPlayerConnectionChange(message, true);
            case "RECONNECT" -> onPlayerConnectionChange(message, false);
            default -> {}
        }
    }

    public void onPlayerConnectionChange(Message message, boolean disconnected) {
        String username = new JSONObject(message.getBody()).getString("username");
        view.updatePlayerConnectionStatus(username, disconnected);
    }

    public void onListReceived(Message message) {
        JSONObject body = new JSONObject(message.getBody());
        JSONArray files = body.getJSONArray("files");
        if (files.isEmpty()) view.showListOfSavedGames(null);
        else {
            String[] parsedFiles = new String[files.length()];
            for (int i = 0; i < parsedFiles.length; i++) {
                parsedFiles[i] = "Saved game " + parseSavedGameDateFormat(files.getString(i));
            }
            view.showListOfSavedGames(parsedFiles);
        }
    }

    private String parseSavedGameDateFormat(String filename) {
        String millisecondsString = filename.substring(filename.indexOf("_") + 1, filename.indexOf(".json"));
        long millis = Long.parseLong(millisecondsString);
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        Date date = new Date(millis);

        return dateFormat.format(date);
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
        client.sendRequest("PICK", body.toString());
    }


    public void onChatMessageSent(String message) {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        body.put("message", message);
        client.sendRequest("CHAT", body.toString());
    }


    public void onChatWhisperSent(String message, String recipient) {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        body.put("message", message);
        body.put("recipient", recipient);
        client.sendRequest("CHAT", body.toString());
    }

    public void onEndOfTurn() {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        client.sendRequest("NEXT", body.toString());
    }
}
