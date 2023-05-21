package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.ViewMessage;
import it.polimi.ingsw.client.view.cli.graphics.util.ICliRenderer;
import it.polimi.ingsw.client.view.cli.graphics.util.SimpleColorRenderer;
import it.polimi.ingsw.client.view.cli.graphics.util.SimpleTextRenderer;
import it.polimi.ingsw.util.observer.ViewListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;

// TODO
public class ViewCli extends View {
    private final PrintStream out = new PrintStream(System.out);
    private DefaultCliGraphics graphics = new DefaultCliGraphics();
    private final static SimpleColorRenderer colorRenderer = new SimpleColorRenderer();
    private final static SimpleTextRenderer textRenderer = new SimpleTextRenderer();

    public ViewCli() {
        this.init();
    }

    @Override
    public void reset() {
        graphics = new DefaultCliGraphics();
    }

    // TODO
    /*@Override*/
    public void run() {
        running = true;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (running) {
            try {
                String line = reader.readLine();
                Input input = parseInputString(line);
                parseInput(input);
            } catch (IOException e) {
                System.out.println("Client is closing, reason: " + e.getMessage());
                return;
            }
        }
    }

    private record Input(String command, String[] args) {}

    private Input parseInputString(String input) {
        String[] tokens = input.split(" ");
        String[] args = new String[]{""};
        if (tokens.length > 1) args = Arrays.copyOfRange(tokens, 1, tokens.length);
        return new Input(tokens[0], args);
    }

    private int[] parseBoardCoordinates(String input) {
        char rowChar = input.charAt(0);
        int row, col;

        row = rowChar - 'A';
        col = Integer.parseInt(input.substring(1)) - 1;

        return new int[]{row, col};
    }

    public void init() {
        out.println("Welcome to MyShelfie game!");
        this.askUsername();
    }

    public void askUsername() {
        out.print("Enter your nickname (type name + your name):\n");
    }

    public void askTypeOfGame() {
        out.println("""
                Please enter:
                'load' + the name of the file if you want to load a saved game
                'new' + the number of players to create a new game
                'join' if you want to join a game in progress
                'list' if you want to list all saved games"""
        );
    }

    private boolean checkInput(String input, int inferior, int superior) {
        try {
            int number = Integer.parseInt(input);
            if ((number < inferior) || (number > superior)) {
                System.out.println("Invalid input, please enter a number from " + inferior + " to " + superior);
            } else {
                return true;
            }
        } catch (NumberFormatException e) {
            System.out.println("Input is empty");
        }
        return false;
    }

    public void askCoordinatesTilesOnBoard() {
        out.println("Enter 'pick' + row coordinate (you can select a char among A and I)" +
                " + column number (you can select a number among 1 and 9)\nExample: pick A1 A3");
    }

    public void askOrderTiles() {
        out.println("""
                Enter 'order' + the number of the tile you want to put in first position
                 + the number of the tile you want to put in second position
                 + the number of the tile you want to put in third position
                Example: order 3 2 1
                If you have only 1 or 2 tiles put 0""");
    }

    public void askNumberOfColumn() {
        out.println("Enter 'drop' + column number (you can select a number among 1 and 5)\nExample: drop 1");
    }

    private void parseInput(Input input) {
        switch (input.command().toLowerCase()) {
            case "name" -> {
                if (!input.args[0].isEmpty()) {
                    notifyNameChange(input.args[0]);
                    askTypeOfGame();
                }
            }
            case "list" -> notifyListeners(ViewListener::onListSavedGames);
            case "new" -> {
                if (checkInput(input.args[0], 2, 4))
                    notifyNewGameCommand(Integer.parseInt(input.args[0]));
            }
            case "join" -> notifyJoinGameCommand();
            case "quit" -> notifyQuitGameCommand();
            case "load" -> notifyLoadCommand(Integer.parseInt(input.args[0])); // TODO: this will crash (see other todos)
            case "save" -> notifySaveCommand();
            case "/m" -> notifyNewChatMessage(String.join(" ", input.args));
            case "/w" ->
                    notifyNewChatWhisper(String.join(" ", Arrays.copyOfRange(input.args, 1, input.args.length)), input.args[0]);
            case "pick" -> {
                if (input.args.length > 1) {
                    int[] coords1, coords2;
                    coords1 = parseBoardCoordinates(input.args[0].toUpperCase());
                    coords2 = parseBoardCoordinates(input.args[1].toUpperCase());
                    notifyPickCommand(coords1[0], coords1[1], coords2[0], coords2[1]);
                    askOrderTiles();
                }
            }
            case "order" -> {
                if (input.args.length == 3 && checkInput(input.args[0], 1, 3) && checkInput(input.args[1], 0, 3) && checkInput(input.args[2], 0, 3)) {
                    notifyOrderCommand(Integer.parseInt(input.args[0]), Integer.parseInt(input.args[1]), Integer.parseInt(input.args[2]));
                    askNumberOfColumn();
                }
            }
            case "drop" -> {
                if (checkInput(input.args[0], 1, 5)) {
                    notifyDropCommand(Integer.parseInt(input.args[0]) - 1);
                    notifyListeners(ViewListener::onEndOfTurn);
                }
            }
            case "help" -> System.out.println("""
                    Possible commands:
                     - name + username          | set the name of the player
                     - list                     | show a list of saved games
                     - new + number of players  | start a new game with N players
                     - join                     | join a game
                     - quit                     | quit game
                     - load + name of saved game| load a saved game
                     - save                     | save the game
                     - /m + message             | send a message to the others players
                     - /w + username + message  | send a message to another player
                     - pick + A1 + A1           | pick up tiles in the chosen range
                     - order + number of first tile + number of second tile + number of third tile | order tiles in the chosen way
                     - drop + number of column  | drop tiles into the chosen column of the bookshelf""");
            default -> notifyGenericInput(input.command + " " + String.join(" ", input.args));
        }
    }

    @Override
    public void updateGameStatus(boolean isGameOver) {
        graphics.updateGameStatus(isGameOver);
    }

    @Override
    public void updatePlayerScore(String player, int score) {
        graphics.updatePlayerScore(player, score);
    }

    @Override
    public void updateBookshelf(String player, int[][] update) {
        graphics.updateBookshelf(player, update);
    }

    @Override
    public void updateBoard(int[][] update) {
        graphics.updateBoard(update);
    }

    @Override
    public void updatePlayerConnectionStatus(String player, boolean isDisconnected) {
        graphics.updatePlayerConnectionStatus(player, isDisconnected);
    }

    @Override
    public void updateCommonGoalPoints(int cardId, int points) {
        graphics.updateCommonGoalPoints(cardId, points);
    }

    @Override
    public void setCommonGoal(int id, int points) {
        graphics.setCommonGoal(id, points);
    }

    @Override
    public void setPersonalGoal(int id) {
        graphics.setPersonalGoal(id);
    }

    @Override
    public void setCurrentTurn(int turn) {
        graphics.setCurrentTurn(turn);
    }

    @Override
    public void addMessage(String message, String sender, boolean isWhisper) {
        graphics.addMessage(message, sender, isWhisper);
    }

    @Override
    public void addPlayer(String username, int score, boolean isClient) {
        graphics.addPlayer(username, score, isClient);
    }

    @Override
    public void finalizeUpdate() {
        // Clear console
        out.print("\033[H\033[2J");
        out.flush();

        ICliRenderer renderer = colorRenderer;

        // Check if view is running in a windows' console, in that case don't use colors
        // (ANSI codes are not supported by windows' powershell)
        if (System.getProperty("os.name").contains("win")) renderer = textRenderer;

        out.println(graphics.getGraphics().render(renderer));
    }

    @Override
    public void showListOfSavedGames(String[] savedGames) {
        System.out.println("Saved games:");
        if (savedGames == null) {
            System.out.println("No saved games found");
            return;
        }

        for (int i = 0; i < savedGames.length; i++) {
            System.out.printf("%d. %s%n", (i+1), savedGames[i]);
        }
    }

    @Override
    public void handleServerConnectionError(String message) {
        System.out.println(message);
    }

    @Override
    public void handleErrorMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void handleSuccessMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void allowUsersGameCommands() {
        super.allowUsersGameCommands();
        askCoordinatesTilesOnBoard();
    }
}
