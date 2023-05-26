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
    private final Client client;
    private String myUsername;
    private ClientBoard board;
    private List<ClientPlayer> players;
    private List<ClientCommonGoalCard> commonGoalCards;
    private ClientTurnState turnState;
    private final View view;
    private int row1, row2, col1, col2, first, second, third;
    private boolean clientIsInGame;


    public ClientController(View view, String serverAddress) {
        this.view = view;
        view.setClientController(this);
        clientIsInGame = false;

        client = new Client(serverAddress);


        client.setOnMessageReceivedCallback(this::onMessageReceived);
        client.setOnConnectionLostCallback((msg) -> {
            clientIsInGame = false;
            view.handleServerConnectionError(msg);
            resetVirtualModelAndView();
        });

//        client.start();

        view.setClient(client);
        view.addListener(this);
        view.start();
    }

    public void onMessageReceived(Message message) {
        String method = message.getMethod();
        JSONObject body = new JSONObject(message.getBody());

        switch (method) {
            case "START" -> onGameStart(message);
            case "UPDATE" -> {
                if (body.has("reconnected")) onGameStart(message);
                else update(message);
            }
            case "CHAT" -> onChatMessageReceived(message);
            case "LIST" -> onListReceived(message);
            case "OK" -> view.handleSuccessMessage(body.getString("msg"));
            case "ERR" -> view.handleErrorMessage(body.getString("msg"));
            case "NAME" -> {
                myUsername = body.getString("username");
                view.handleSuccessMessage("NAME");
            }
        }
    }

    public void handleDisconnections(JSONObject body) {
        if (!body.has("disconnected_players") || !body.has("connected_players")) return;
        JSONArray disconnectedPlayers = body.getJSONArray("disconnected_players");
        JSONArray connectedPlayers = body.getJSONArray("connected_players");

        for (int i = 0; i < disconnectedPlayers.length(); i++) {
            String username = disconnectedPlayers.getString(i);
            if (username.equals(myUsername)) {
                clientIsInGame = false;
                return;
            }
            view.updatePlayerConnectionStatus(username, true);
        }

        for (int i = 0; i < connectedPlayers.length(); i++) {
            String username = connectedPlayers.getString(i);
            view.updatePlayerConnectionStatus(username, false);
        }

        if (!clientIsInGame) return;
        view.finalizeUpdate();
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
        if (!clientIsInGame) return;

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
        if (!clientIsInGame) return;

        JSONObject body = new JSONObject(message.getBody());
        JSONObject serialized = body.getJSONObject("serialized");
        JSONArray jsonPlayers = serialized.getJSONArray("players");
        JSONArray jsonCommonGoals = serialized.getJSONArray("common_goals");

        handleDisconnections(body);
        board.updateBoard(serialized.toString());
        turnState.updateTurnState(body.toString());

        for (int i = 0; i < jsonPlayers.length(); i++) {
            ClientPlayer player = players.get(i);
            player.updatePlayer(jsonPlayers.getJSONObject(i).toString());
        }

        for (int i = 0; i < jsonCommonGoals.length(); i++) {
            commonGoalCards.get(i).updatePoints(jsonCommonGoals.getJSONObject(i).toString());
        }

        // if it's the client's turn, allow commands
        if (turnState.getCurrentPlayer().equals(myUsername)) {
            view.allowUsersGameCommands();
            view.handleSuccessMessage("Your turn to play");
        } else {
            view.blockUsersGameCommands();
        }

        view.finalizeUpdate();
        if (body.has("winner")) {
            System.out.println("YOOOO we have a winner!");
            view.handleWinnerSelected(body.getString("winner"));
            // reset model and view
            resetVirtualModelAndView();
            clientIsInGame = false;
        }
    }

    private void resetVirtualModelAndView() {
        board = new ClientBoard();
        turnState = new ClientTurnState();
        players = new LinkedList<>();
        commonGoalCards = new LinkedList<>();
        view.reset();
    }

    public void onGameStart(Message message) {
        JSONObject body = new JSONObject(message.getBody());
        JSONArray players = body.getJSONObject("serialized").getJSONArray("players_order");
        for (int i = 0; i < players.length(); i++) {
            if (players.getString(i).equals(myUsername)) {
                clientIsInGame = true;
                break;
            }
        }

        if (!clientIsInGame) return;

        initVirtualModelAndView(body, message);

    }

    private void initVirtualModelAndView(JSONObject body, Message message) {
        board = new ClientBoard();
        turnState = new ClientTurnState();
        players = new LinkedList<>();
        commonGoalCards = new LinkedList<>();

        Runnable runLater = ()->{
            setupGameFromJson(body);
            setupModelListeners();
            update(message);
        };

        view.startGameView(runLater);
    }

    private void setupModelListeners() {
        board.addListener(view);
        turnState.addListener(view);
        players.forEach(player -> player.addListener(view));
        commonGoalCards.forEach(goal -> goal.addListener(view));
    }

    public void setupGameFromJson(JSONObject gameState) {
        JSONObject serialized = gameState.getJSONObject("serialized");
        JSONArray jsonPlayers = serialized.getJSONArray("players");
        JSONArray jsonCommonGoals = serialized.getJSONArray("common_goals");

        addPlayers(jsonPlayers);
        addCommonGoals(jsonCommonGoals);
    }

    private void addPlayers(JSONArray jsonPlayers) {
        for (int i = 0; i < jsonPlayers.length(); i++) {
            JSONObject playerObject = jsonPlayers.getJSONObject(i);
            String username = playerObject.getString("username");
            boolean isCurrentClient = username.equals(myUsername);

            ClientPlayer virtualPlayer = new ClientPlayer(username);
            virtualPlayer.updatePlayer(playerObject.toString());
            virtualPlayer.updatePersonalCardId(playerObject.toString());

            players.add(virtualPlayer);
            view.addPlayer(username, 0, isCurrentClient);
            if (isCurrentClient) view.setPersonalGoal(virtualPlayer.getPersonalCardId());
        }
    }

    private void addCommonGoals(JSONArray jsonCommonGoals) {
        for (int i = 0; i < jsonCommonGoals.length(); i++) {
            JSONObject goalObject = jsonCommonGoals.getJSONObject(i);

            ClientCommonGoalCard virtualCommonGoal = new ClientCommonGoalCard();
            virtualCommonGoal.updateId(goalObject.toString());

            commonGoalCards.add(virtualCommonGoal);
            view.setCommonGoal(virtualCommonGoal.getId(), 8);
        }
    }

    public void onJoinGame() {
        if (clientIsInGame) {
            view.handleErrorMessage("Cannot perform this action while in game");
            return;
        }

        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        client.sendRequest("JOIN", body.toString());
    }


    public void onNewGame(int lobbySize) {
        if (clientIsInGame) {
            view.handleErrorMessage("Cannot perform this action while in game");
            return;
        }

        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        body.put("lobby_size", lobbySize);
        client.sendRequest("NEW", body.toString());
    }


    public void onListSavedGames() {
        if (clientIsInGame) {
            view.handleErrorMessage("Cannot perform this action while in game");
            return;
        }

        // This should be useless now that a username
        // is not needed for getting the list of games,
        // but it isn't harming anyone, so it's staying <3
        JSONObject body = new JSONObject();
        body.put("username", myUsername);


        client.sendRequest("LIST", body.toString());
    }


    public void onLoadSavedGame(int saveIndex) {
        if (clientIsInGame) {
            view.handleErrorMessage("Cannot perform this action while in game");
            return;
        }

        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        body.put("save_index", saveIndex - 1);
        client.sendRequest("LOAD", body.toString());
    }


    public void onSaveCurrentGame() {
        if (!clientIsInGame) {
            view.handleErrorMessage("Cannot perform this action when not in game");
            return;
        }

        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        client.sendRequest("SAVE", body.toString());
    }


    public void onQuitGame() {
        if (!clientIsInGame) {
            view.handleErrorMessage("Cannot perform this action when not in game");
            return;
        }

        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        client.sendRequest("QUIT", body.toString());

        // reset local data
        resetVirtualModelAndView();
        clientIsInGame = false;
    }


    public void onChooseUsername(String username) {
        if (clientIsInGame) {
            view.handleErrorMessage("Cannot perform this action while in game");
            return;
        }

        client.sendRequest("NAME", new JSONObject().put("username", username).toString());
    }

    public void onChooseColumnOfBookshelf(int numberOfColumn) {
        if (!clientIsInGame) {
            view.handleErrorMessage("Cannot perform this action when not in game");
            return;
        }

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
        if (!clientIsInGame) {
            view.handleErrorMessage("Cannot perform this action when not in game");
            return;
        }

        this.first = first;
        this.second = second;
        this.third = third;
    }

    public void onChooseTilesOnBoard(int row1, int col1, int row2, int col2) {
        if (!clientIsInGame) {
            view.handleErrorMessage("Cannot perform this action when not in game");
            return;
        }

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
        this.first = 1;
        this.second = 2;
        this.third = 3;
        client.sendRequest("PICK", body.toString());
    }


    public void onChatMessageSent(String message) {
        if (!clientIsInGame) {
            view.handleErrorMessage("Cannot send a message when not in game");
            return;
        }

        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        body.put("message", message);
        client.sendRequest("CHAT", body.toString());
    }


    public void onChatWhisperSent(String message, String recipient) {
        if (!clientIsInGame) {
            view.handleErrorMessage("Cannot send a message when not in game");
            return;
        }

        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        body.put("message", message);
        body.put("recipient", recipient);
        client.sendRequest("CHAT", body.toString());
    }

    public void onEndOfTurn() {
        if (!clientIsInGame) return;

        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        client.sendRequest("NEXT", body.toString());
    }

    public void onGenericInput(String input) {
        if (!client.isConnected()) {
            if (input.trim().equalsIgnoreCase("y")) client.start();
            else view.shutdown();
            return;
        }

        view.handleErrorMessage("No command " + input + " found");
    }
}
