package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.view.cli.CliView;
import it.polimi.ingsw.client.virtualModel.ClientBoard;
import it.polimi.ingsw.client.virtualModel.ClientBookshelf;
import it.polimi.ingsw.client.virtualModel.ClientCommonGoalCard;
import it.polimi.ingsw.client.virtualModel.ClientPlayer;
import it.polimi.ingsw.server.messages.Message;
import it.polimi.ingsw.util.observer.ControllerObserver;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class ClientController implements ControllerObserver {

    private final static int SERVER_PORT = 49152;
    private Client client;
    private String myUsername; 
    private ClientBoard board = new ClientBoard();
    private final List<ClientPlayer> players = new LinkedList<>();
    private final List<ClientCommonGoalCard> commonGoalCards = new LinkedList<>();
    private final CliView view;

    public ClientController(CliView view, String serverAddress){
        this.view = view;
        client = new Client(serverAddress, SERVER_PORT);
        client.addObserver(this);
        view.addObserver(this);

        client.setOnMessageReceivedCallback(this::onMessageReceived);
        client.start();
        new Thread(view).start();
    }

    @Override
    public void onMessageReceived(Message message) {
        String method = message.getMethod();
        switch (method) {
            case "START" -> onGameStart(message);
            case "UPDATE" -> update(message);
            case "CHAT" -> onChatMessageReceived(message);
            case "LIST" -> onListReceived(message);
            case "LEFT" -> System.out.println(new JSONObject(message.getBody()).getString("username") + " left the game");
            case "OK", "ERR" -> System.out.println(new JSONObject(message.getBody()).getString("msg"));
            default -> {}
        }
    }

    @Override
    public void onListReceived(Message message) {
        JSONObject body = new JSONObject(message.getBody());
        JSONArray files = body.getJSONArray("files");

        // TODO: notify view of list / print list.
    }

    @Override
    public void onChatMessageReceived(Message message) {
        JSONObject body = new JSONObject(message.getBody());
        String chatMessage = body.getString("message");
        String sender = body.getString("username");
        String recipient = null;
        if (body.has("recipient")) recipient = body.getString("recipient");

        if (recipient != null && recipient.equals(myUsername)) view.addMessage(chatMessage, sender, true);
        else if (recipient == null) view.addMessage(chatMessage, sender, false);

        view.drawGraphics();
    }

    @Override
    public void update(Message message) {
        JSONObject body = new JSONObject(message.getBody());

        if (!body.has("serialized")) {
            System.out.println("Ue guarda che non c'è serialized");
            return;
        }

        JSONObject serialized = body.getJSONObject("serialized");
        JSONArray jsonPlayers = serialized.getJSONArray("players");
        JSONArray jsonCommonGoals = serialized.getJSONArray("common_goals");

        board.updateBoard(serialized.toString());
        view.updateBoard(board.getSpaces());

        for (int i = 0; i < jsonPlayers.length(); i++) {
            JSONObject currPlayer = jsonPlayers.getJSONObject(i);
            String currUsername = currPlayer.getString("username");
            ClientPlayer player = players.get(i);

            player.updatePlayer(jsonPlayers.getJSONObject(i).toString());

            ClientBookshelf bookshelf = player.getBookshelf();
            view.updateBookshelf(currUsername, bookshelf.getTiles());
            view.updatePlayerScore(player.getUsername(), player.getScore());
        }

        for (int i = 0; i < jsonCommonGoals.length(); i++) {
            commonGoalCards.get(i).updateId(jsonCommonGoals.getJSONObject(i).toString());
            view.updateCommonGoalPoints(commonGoalCards.get(i).getId(), commonGoalCards.get(i).getScore());
        }

        view.drawGraphics();
    }

    @Override
    public void onGameStart(Message message) {
        JSONObject body = new JSONObject(message.getBody());
        JSONObject serialized = body.getJSONObject("serialized");
        JSONArray players = serialized.getJSONArray("players");
        JSONArray commonGoals = serialized.getJSONArray("common_goals");

        for (int i = 0; i < players.length(); i++) {
            JSONObject player = players.getJSONObject(i);
            this.players.add(new ClientPlayer(player.getString("username")));
            view.addPlayer(player.getString("username"), player.getInt("score"), player.getString("username").equals(myUsername));
            if (player.getString("username").equals(myUsername)) view.setPersonalGoal(player.getInt("personal_card_id"));
        }

        for (int i = 0; i < commonGoals.length(); i++) {
            this.commonGoalCards.add(new ClientCommonGoalCard());
            view.setCommonGoal(commonGoals.getJSONObject(i).getInt("card_id"), 0);
        }

        this.board = new ClientBoard();

        update(message);
    }

    @Override
    public void joinGame() {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        client.sendRequest("JOIN", body.toString());
    }

    @Override
    public void newGame(int lobbySize) {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        body.put("lobby_size", lobbySize);
        client.sendRequest("NEW", body.toString());
    }

    @Override
    public void listSavedGames() {
        client.sendRequest("LIST", "");
    }

    @Override
    public void loadSavedGame(String saveName) {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        body.put("save_name", saveName);
        client.sendRequest("LOAD", body.toString());
    }

    @Override
    public void saveCurrentGame() {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        client.sendRequest("SAVE", body.toString());
    }

    @Override
    public void quitGame() {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        client.sendRequest("QUIT", body.toString());
    }

    @Override
    public void onChooseUsername(String username) {
        System.out.println("updated username : " + username);
        this.myUsername = username;
    }

    @Override
    public void onChoosePlayersNumber(int playersNumber) {

    }

    @Override
    public void onChooseColumnOfBookshelf(int numberOfColumn) {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        body.put("row1", row1);
        body.put("row2", row2);
        body.put("col1", col1);
        body.put("col2", col2);
        body.put("column", numberOfColumn);
        System.out.println("sending message");
        client.sendRequest("DROP", body.toString());
    }

    @Override
    public void onChooseTilesOrder(int first, int second, int third) {

    }

    private int row1, row2, col1, col2;

    @Override
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

    @Override
    public void onChatMessageSent(String message) {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        body.put("message", message);
        System.out.println("sending message");
        client.sendRequest("CHAT", body.toString());
    }

    @Override
    public void onChatWhisperSent(String message, String recipient) {
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        body.put("message", message);
        body.put("recipient", recipient);
        System.out.println("sending whisper");
        client.sendRequest("CHAT", body.toString());
    }
}
