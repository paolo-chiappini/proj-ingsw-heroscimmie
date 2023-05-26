package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.cli.graphics.util.ICliRenderer;
import it.polimi.ingsw.client.view.cli.graphics.util.SimpleColorRenderer;
import it.polimi.ingsw.client.view.cli.graphics.util.SimpleTextRenderer;
import it.polimi.ingsw.util.observer.ViewListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;

public class ViewCli extends View {
    private final PrintStream out = new PrintStream(System.out);
    private DefaultCliGraphics graphics = new DefaultCliGraphics();
    private final static SimpleColorRenderer colorRenderer = new SimpleColorRenderer();
    private final static SimpleTextRenderer textRenderer = new SimpleTextRenderer();
    private boolean hasPickedTiles, lastInputGeneratedError;
    private int[] coords1 = new int[2];
    private int[] coords2 = new int[2];
    private int numberOfTilesPickedUp = 0;

    public ViewCli() {
        hasPickedTiles = false;
        lastInputGeneratedError = false;
        this.init();
    }

    @Override
    public void startGameView(Runnable finishSetup) {
        reset();
        finishSetup.run();
    }

    @Override
    public void reset() {
        graphics = new DefaultCliGraphics();
    }

    // TODO
    /*@Override*/
    public void run() {
        connectToServer();
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
        // guard against escape chars
        if (input == null || input.isEmpty()) return new Input("", new String[]{""});

        String[] tokens = input.split(" ");
        String[] args = new String[]{""};
        if (tokens.length > 1) args = Arrays.copyOfRange(tokens, 1, tokens.length);
        return new Input(tokens[0], args);
    }

    private int[] parseBoardCoordinates(String input) {
        char rowChar = input.charAt(0);
        int row, col;

        row = rowChar - 'A';
        try {
            col = Integer.parseInt(input.substring(1)) - 1;
        }
        catch (NumberFormatException e){
            col = 0;
        }
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
                out.println("Invalid input, please enter a number from " + inferior + " to " + superior);
            } else {
                return true;
            }
        } catch (Exception e) {
           out.println("Input is not valid");
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
                Example: order 1 2 3""");
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
            case "quit" -> {
                notifyQuitGameCommand();
                if (!lastInputGeneratedError) System.out.println("Left game");
            }
            case "load" -> {
                if(!input.args[0].isEmpty()) notifyLoadCommand(Integer.parseInt(input.args[0]));
            }
            case "save" -> notifySaveCommand();
            case "/m" -> notifyNewChatMessage(String.join(" ", input.args));
            case "/w" -> notifyNewChatWhisper(String.join(" ", Arrays.copyOfRange(input.args, 1, input.args.length)), input.args[0]);
            case "pick" -> {
                   checkInputTilesPickUp(input);
                    if(canSendCommands) {
                        finalizeUpdate();
                        if (!lastInputGeneratedError) {
                            numberOfTilesPickedUp = Integer.max(coords2[0] - coords1[0],coords2[1] - coords1[1]) + 1;
                            if (numberOfTilesPickedUp == 1)
                                askNumberOfColumn();
                            else askOrderTiles();
                        } else askCoordinatesTilesOnBoard();
                    }
            }
            case "order" -> {
                checkInputTilesOrder(input, numberOfTilesPickedUp);
                if(canSendCommands) {
                    finalizeUpdate();
                    if (!lastInputGeneratedError) askNumberOfColumn();
                    else askOrderTiles();
                }
            }
            case "drop" -> {
                if (checkInput(input.args[0], 1, 5)) {
                    notifyDropCommand(Integer.parseInt(input.args[0]) - 1);
                    notifyListeners(ViewListener::onEndOfTurn);

                    hasPickedTiles = false;
                    if (lastInputGeneratedError && canSendCommands) askNumberOfColumn();
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
        lastInputGeneratedError = false;
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

    private void flushConsole() {
        out.print("\033[H\033[2J");
        out.flush();
    }

    /**
     * Check if the tiles order entered by the user is valid
     * @param input is user input
     */
    private void checkInputTilesOrder(Input input, int numberOfTilesPickedUp)
    {
        int [] tiles_in_order = new int[3];
        if(!input.args[0].isEmpty() && input.args.length <= 3)
        {
            for(int i=0; i<input.args.length; i++)
            {
                if(checkInput(input.args[i],1,numberOfTilesPickedUp))
                    tiles_in_order[i]= Integer.parseInt(input.args[i]);
                else
                    lastInputGeneratedError = true;
            }
            int i = input.args.length;
            while (i<3)
            {
                tiles_in_order[i]=input.args.length+1;
                i++;
            }
            notifyOrderCommand(tiles_in_order[0], tiles_in_order[1], tiles_in_order[2]);
        }
        else lastInputGeneratedError = true;
    }

    /**
     * Check if the input entered by the user is valid.
     * It also automatically enters the coordinates of the second tile if the user draws only one tile.
     * @param input is user input
     */
    private void checkInputTilesPickUp(Input input) {
        if (!input.args[0].isEmpty() && input.args.length <= 2) {
            if (input.args.length == 1 && input.args[0].length() == 2) {
                coords1 = parseBoardCoordinates(input.args[0].toUpperCase());
                coords2 = coords1;
                notifyPickCommand(coords1[0], coords1[1], coords2[0], coords2[1]);
                hasPickedTiles = true;
            }
            else if (input.args.length == 2 && input.args[0].length()==2 && input.args[1].length()==2) {
                coords1 = parseBoardCoordinates(input.args[0].toUpperCase());
                coords2 = parseBoardCoordinates(input.args[1].toUpperCase());
                notifyPickCommand(coords1[0], coords1[1], coords2[0], coords2[1]);
                hasPickedTiles = true;
            }
          else lastInputGeneratedError = true;
        }
        else lastInputGeneratedError = true;
    }

    @Override
    public void finalizeUpdate() {
        flushConsole();

        ICliRenderer renderer = colorRenderer;

        // Check if view is running in a windows' console, in that case don't use colors
        // (ANSI codes are not supported by windows' powershell)
        if (System.getProperty("os.name").contains("win")) renderer = textRenderer;

        out.println(graphics.getGraphics().render(renderer));
        if (canSendCommands && !hasPickedTiles && !lastInputGeneratedError) askCoordinatesTilesOnBoard();
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
    public void handleWinnerSelected(String winner) {
        System.out.printf("%s WON THE GAME!!!%n", winner);
        System.out.println("Game is over, if you wish to start a new game use the 'help' command");
    }

    @Override
    public void handleErrorMessage(String message) {
        lastInputGeneratedError = true;
        System.out.println(message);
    }

    @Override
    public void handleSuccessMessage(String message) {
        if(message.equals("NAME")) return;

        System.out.println(message);
    }

    @Override
    public void shutdown() {
        super.shutdown();
        flushConsole();
        System.out.println("Exiting, (＾Ｕ＾)ノ bye");
    }
}
