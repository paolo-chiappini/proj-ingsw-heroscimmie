package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.cli.graphics.util.SimpleColorRenderer;
import it.polimi.ingsw.util.observer.ControllerObserver;
import it.polimi.ingsw.util.observer.ViewObservable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;

public class ViewCli extends ViewObservable implements Runnable {
    private PrintStream out = new PrintStream(System.out);
    private final DefaultCliGraphics graphics = new DefaultCliGraphics();

    public ViewCli(){
        drawGraphics();
        this.init();
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String line = reader.readLine();
                Input input = parseInputString(line);
                parseInput(input);
            } catch (IOException e) {
                throw new RuntimeException(e);
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
        col = Integer.parseInt(input.substring(1))-1;

        return new int[] {row, col};
    }
    
    public void init(){
        out.println("Welcome to MyShelfie game!");
        this.askUsername();
    }

    public void askUsername(){
        out.print("Enter your nickname (type name + your name):\n");
    }

    public void askTypeOfGame(){
        out.println("""
                        Please enter:
                        'load' + the name of the file if you want to load a saved game
                        'new' + the number of players to create a new game
                        'join' if you want to join a game in progress"""
        );
    }

    private boolean checkCorrectInput(String input, int inferior, int superior) {
        try {
            int number = Integer.parseInt(input);
            if ((number < inferior) || (number > superior)) {
                System.out.println("Invalid input, please enter a number from "+inferior+" to "+superior);
            } else {
                return true;
            }
        } catch (NumberFormatException e) {
            System.out.println("Input is empty");
        }
        return false;
    }

    public void askCoordinatesTilesOnBoard(){
        out.println("Enter 'pick' + row coordinate (you can select a char among A and I)" +
                " + column number (you can select a number among 1 and 9)\nExample: pick A1 A3");
    }

    public void askOrderTiles(){
        out.println("""
                Enter 'order' + the number of the tile you want to put in first position
                 + the number of the tile you want to put in second position
                 + the number of the tile you want to put in third position
                Example: order 3 2 1
                If you have only 1 or 2 tiles put 0""");
    }

    public void askNumberOfColumn(){
        out.println("Enter 'drop' + column number (you can select a number among 1 and 5)\nExample: drop 1");
    }

    private void parseInput(Input input) {
        switch (input.command().toLowerCase()) {
            case "name" -> {
                if(!input.args[0].isEmpty()){
                    notifyObservers(controllerObserver -> controllerObserver.onChooseUsername(input.args[0]));
                    askTypeOfGame();
                }
            }
            case "list" -> notifyObservers(ControllerObserver::listSavedGames);
            case "new" -> {
                if(checkCorrectInput(input.args[0],2,4))
                    notifyObservers(controllerObserver -> controllerObserver.newGame(Integer.parseInt(input.args[0])));
            }
            case "join" -> notifyObservers(ControllerObserver::joinGame);
            case "quit" -> notifyObservers(ControllerObserver::quitGame);
            case "load" -> notifyObservers(controllerObserver -> controllerObserver.loadSavedGame(input.args[0]));
            case "save" -> notifyObservers(ControllerObserver::saveCurrentGame);
            case "/m" -> notifyObservers(controllerObserver -> controllerObserver.onChatMessageSent(String.join(" ", input.args)));
            case "/w" -> notifyObservers(controllerObserver -> controllerObserver.onChatWhisperSent(String.join(" ", Arrays.copyOfRange(input.args, 1, input.args.length)), input.args[0]));
            case "pick" -> {
                if(input.args.length>1) {
                    int[] coords1, coords2;
                    coords1 = parseBoardCoordinates(input.args[0].toUpperCase());
                    coords2 = parseBoardCoordinates(input.args[1].toUpperCase());
                    notifyObservers(controllerObserver -> controllerObserver.onChooseTilesOnBoard(coords1[0], coords1[1], coords2[0], coords2[1]));
                    askOrderTiles();
                }
            }
            case "order" -> {
                if(input.args.length>3 && checkCorrectInput(input.args[0],1,3) && checkCorrectInput(input.args[1],0,3)&& checkCorrectInput(input.args[2],0,3)){
                    notifyObservers(controllerObserver -> controllerObserver.onChooseTilesOrder(Integer.parseInt(input.args[0]),Integer.parseInt(input.args[1]),Integer.parseInt(input.args[2])));
                    askNumberOfColumn();
                }
            }
            case "drop" -> {
                if(checkCorrectInput(input.args[0],1,5)){
                    notifyObservers(controllerObserver -> controllerObserver.onChooseColumnOfBookshelf(Integer.parseInt(input.args[0])-1));
                    notifyObservers(ControllerObserver::onEndOfTurn);
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
        }
    }

    public DefaultCliGraphics getGraphics() {
        return graphics;
    }

    public void drawGraphics() {
        out.print("\033[H\033[2J");
        out.flush();
        out.println(graphics.getGraphics().render(new SimpleColorRenderer()));
    }
}
