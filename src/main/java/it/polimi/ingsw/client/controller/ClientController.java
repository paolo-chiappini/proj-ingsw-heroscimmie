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

/**
 * Class responsible for managing communication between the network, model and view.
 * It updates the virtual model, observes view and notify its updates.
 * Furthermore, it receives user input and notifies the network of the request.
 */
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

        //The client network listener is started by the View now

        view.setClient(client);
        view.addListener(this);
        view.start();
    }

    /**
     * It performs an action based on the message received from the server
     * @param message is the message received from the server
     */
    public void onMessageReceived(Message message) {
        String method = message.getMethod();
        JSONObject body = new JSONObject(message.getBody());

        switch (method) {
            case "START" -> onGameStart(message);
            case "UPDATE" -> {
                if (body.has("reconnected")){
                    if(body.get("reconnected").equals(myUsername))  {
                        onGameStart(message);
                        return;
                    }
                }
                update(message);
            }
            case "CHAT" -> onChatMessageReceived(message);
            case "LIST" -> onListReceived(message);
            case "OK" -> view.handleSuccessMessage(body.getString("msg"));
            case "ERR" -> view.handleErrorMessage(body.getString("msg"));
            case "NAME" -> {
                myUsername = body.getString("username");
                view.handleSuccessMessage("NAME"); //The GUI needs to know if the name is ok to proceed
            }
        }
    }

    /**
     * Manages the disconnection of one or more players.
     * @param body is the body of the message received from the server
     */
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

    }

    /**
     * Shows the list of game saves.
     * @param message is the message received from the server
     */
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

    /**
     * Format the game save file name.
     * @param filename is the name of the game save
     */
    private String parseSavedGameDateFormat(String filename) {
        String millisecondsString = filename.substring(filename.indexOf("_") + 1, filename.indexOf(".json"));
        long millis = Long.parseLong(millisecondsString);
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        Date date = new Date(millis);

        return dateFormat.format(date);
    }

    /**
     * Shows the message received from other users in the chat.
     * @param message is the message received from the server
     */
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

    /**
     * Updates the virtual model, the view of the game and the turn of the player.
     * @param message is the message received from the server
     */
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
            view.handleWinnerSelected(body.getString("winner"));
            // reset model and view
            resetVirtualModelAndView();
            clientIsInGame = false;
        }
    }

    //Differentiation between initialization and reset of the view and virtual model
    /**
     * Resets the virtual model and the game view.
     */
    private void resetVirtualModelAndView() {
        board = new ClientBoard();
        turnState = new ClientTurnState();
        players = new LinkedList<>();
        commonGoalCards = new LinkedList<>();
        view.reset();
    }

    /**
     * Initializes the virtual model by instantiating all game objects.
     */
    private void initVirtualModelAndView(JSONObject body, Message message) {
        board = new ClientBoard();
        turnState = new ClientTurnState();
        players = new LinkedList<>();
        commonGoalCards = new LinkedList<>();

        //DOUBLE DISPATCH™ BABY
        //the view needs to be initialized for dealing with updates,
        //so the final part of the initialization is started by the view itself
        Runnable runLater = ()->{
            setupGameFromJson(body);
            setupModelListeners();
            update(message);
        };

        view.startGameView(runLater);
    }

    /**
     * Initializes the game view when server starts the game
     * @param message is the message received from the server
     */
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

    /**
     * Adds listeners (view) to the virtual model
     */
    private void setupModelListeners() {
        board.addListener(view);
        turnState.addListener(view);
        players.forEach(player -> player.addListener(view));
        commonGoalCards.forEach(goal -> goal.addListener(view));
    }

    /**
     * Adds players and common goal cards to the game
     * @param gameState is the state of the game received from the server
     */
    public void setupGameFromJson(JSONObject gameState) {
        JSONObject serialized = gameState.getJSONObject("serialized");
        JSONArray jsonPlayers = serialized.getJSONArray("players");
        JSONArray jsonCommonGoals = serialized.getJSONArray("common_goals");

        addPlayers(jsonPlayers);
        addCommonGoals(jsonCommonGoals);
    }

    /**
     * Adds players and the personal goal card to the virtual model
     * @param jsonPlayers contains the list of players
     */
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

    /**
     * Adds common goal card to the virtual model
     * @param jsonCommonGoals contains common goal cards to be used in the game
     */
    private void addCommonGoals(JSONArray jsonCommonGoals) {
        for (int i = 0; i < jsonCommonGoals.length(); i++) {
            JSONObject goalObject = jsonCommonGoals.getJSONObject(i);

            ClientCommonGoalCard virtualCommonGoal = new ClientCommonGoalCard();
            virtualCommonGoal.updateId(goalObject.toString());

            commonGoalCards.add(virtualCommonGoal);
            view.setCommonGoal(virtualCommonGoal.getId(), 8);
        }
    }

    /**
     * Sends a request to the server to join the game.
     */
    public void onJoinGame() {
        if (clientIsInGame) {
            view.handleErrorMessage("Cannot perform this action while in game");
            return;
        }

        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        client.sendRequest("JOIN", body.toString());
    }

    /**
     * Sends a request to the server to start a new game.
     * @param lobbySize is the number of players chosen
     */
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

    /**
     * Sends a request to the server to get the list of game saves.
     */
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

    /**
     * Sends the request to load a game save to the server.
     */
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

    /**
     * Sends the request to save the game to the server.
     */
    public void onSaveCurrentGame() {
        if (!clientIsInGame) {
            view.handleErrorMessage("Cannot perform this action when not in game");
            return;
        }

        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        client.sendRequest("SAVE", body.toString());
    }

    /**
     * Sends the request to quit the game to the server.
     */
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

    /**
     * Sends a request to the server with the username chosen by the user.
     * @param username is the nickname chosen by the user
     */
    public void onChooseUsername(String username) {
        if (clientIsInGame) {
            view.handleErrorMessage("Cannot perform this action while in game");
            return;
        }

        client.sendRequest("NAME", new JSONObject().put("username", username).toString());
    }

    /**
     * Sends a request to the server with the number of column of the bookshelf where to insert the tiles.
     * @param numberOfColumn is the number of the column where you drop the tiles
     */
    public void onChooseColumnOfBookshelf(int numberOfColumn) {
        if (!clientIsInGame) {
            view.handleErrorMessage("Cannot perform this action when not in game");
            return;
        }

        var body = setJsonObjectForMoveRequest();

        body.put("first_tile", first);
        body.put("second_tile", second);
        body.put("third_tile", third);
        body.put("column", numberOfColumn);
        client.sendRequest("DROP", body.toString());
    }

    /**
     * Sends a request to the server with the order of the tiles.
     * @param first is the position of the first tile to drop
     * @param second is the position of the second tile to drop
     * @param third is the position of the third tile to drop
     */
    public void onChooseTilesOrder(int first, int second, int third) {
        if (!clientIsInGame) {
            view.handleErrorMessage("Cannot perform this action when not in game");
            return;
        }

        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * Sends a request to the server with the range of tiles to pick up.
     * @param row1 is the row index of the first tile to pick up
     * @param col1 is the column index of the first tile to pick up
     * @param row2 is the row index of the last tile to pick up
     * @param col2 is the column index of the last tile to pick up
     */
    public void onChooseTilesOnBoard(int row1, int col1, int row2, int col2) {
        if (!clientIsInGame) {
            view.handleErrorMessage("Cannot perform this action when not in game");
            return;
        }

        this.row1 = row1;
        this.col1 = col1;
        this.row2 = row2;
        this.col2 = col2;
        this.first = 1;
        this.second = 2;
        this.third = 3;
        var body = setJsonObjectForMoveRequest();
        client.sendRequest("PICK", body.toString());
    }

    public JSONObject setJsonObjectForMoveRequest(){
        JSONObject body = new JSONObject();
        body.put("username", myUsername);
        body.put("row1", row1);
        body.put("row2", row2);
        body.put("col1", col1);
        body.put("col2", col2);

        return body;
    }

    /**
     * Send a chat request to the server with the message text
     * @param message is the text of the message to send
     */
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

    /**
     * Sends a chat request to the server with the message text and the recipient.
     * @param message is the text of the message to send
     * @param recipient is the recipient of the message
     */
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

    /**
     * Handles the insertion of an unknown input and client reconnect request.
     * @param input is the input of the user
     */
    public void onGenericInput(String input) {
        if (!client.isConnected()) {
            if (input.trim().equalsIgnoreCase("y")) client.start();
            else view.shutdown();
            return;
        }

        view.handleErrorMessage("No command " + input + " found");
    }
}
