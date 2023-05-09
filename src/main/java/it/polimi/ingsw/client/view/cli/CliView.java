package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.cli.graphics.util.SimpleColorRenderer;
import it.polimi.ingsw.util.observer.ControllerObserver;
import it.polimi.ingsw.util.observer.ViewObservable;
import it.polimi.ingsw.util.observer.ViewObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

public class CliView extends ViewObservable implements ViewObserver, Runnable {
    private Scanner in = new Scanner(System.in);
    private PrintStream out = new PrintStream(System.out);
    private String nickname;
    private int numOfPlayers, col1, col2, row1, row2;
    private final DefaultCliGraphics graphics = new DefaultCliGraphics();

    public CliView(){
        /*this.init();*/
        drawGraphics();
    }

    /**
     * Actions performed when the application starts
     */
    public void init(){
        out.println("Welcome to My Shelfie game!");
        this.askUsername();
        this.askTypeOfGame();
    }

    /**
     * User needs to provide a nickname to jon the match
     */
    public void askUsername(){
        System.out.print("Enter your nickname: ");
        String nickname = String.valueOf(in);
        //notify observer
    }

    /**
     * Asks user how many players are going to join the match
     */
    public void askNumOfPlayers(){
        System.out.println("How many players are going to play?\n Please enter a number from 2 to 4\n");
        numOfPlayers = in.nextInt();
        while(numOfPlayers < 2 || numOfPlayers > 4) {
            System.out.println("Invalid input, please enter a number from 2 to 4");
            numOfPlayers = in.nextInt();
        }
        //notify observer
    }

    public void askTypeOfGame(){
        System.out.println("Please enter: " +
                "'L' if you want to load a saved game\n" +
                "'N' to create a new game\n" +
                "'J' if you want to join a game in progress\n");
        String gameType = String.valueOf(in);
        if(gameType.equalsIgnoreCase("l")){
            System.out.println("You chose to load a game");
            //loadGame
        }
        else if(gameType.equalsIgnoreCase("n")){
            System.out.println("You chose to create a new game");
            this.askNumOfPlayers();
            //create new game
        }
        else if (gameType.equalsIgnoreCase("j")){
            System.out.println("You chose to join a game");
            //join game
        }
    }

    /**
     * Ask player the range of column they want to select
     */
    public void askColumnNumber(){
        if(numOfPlayers == 2){
            System.out.println("Enter column number 1 (you can select a number among 1 and 7):\n ");
            col1 = in.nextInt();
            do{
                System.out.println("You entered an invalid number :(" +
                        "Enter a number among 1 and 7");
                col1 = in.nextInt();
            }while (col1 < 1 || col1 > 7);

            System.out.println("Please enter column number 2");
            col2 = in.nextInt();
            do{
                System.out.println("You entered an invalid number :(" +
                        "Enter a number among 1 and 7");
                col2 = in.nextInt();
            }while (col2 < 1 || col2 > 7);
        }
        else if(numOfPlayers == 3 || numOfPlayers == 4){
            System.out.println("Enter column number 1 (you can select a number among 0 and 8):\n ");
            col1 = in.nextInt();
            do{
                System.out.println("You entered an invalid number :(" +
                        "Enter a number among 1 and 7");
                col1 = in.nextInt();
            }while (col1 < 0 || col1 > 8);

            System.out.println("Please enter column number 2");
            col2 = in.nextInt();
            do{
                System.out.println("You entered an invalid number :(" +
                        "Enter a number among 1 and 7");
                col2 = in.nextInt();
            }while (col2 < 0 || col2 > 8);
        }

    }

    /**
     * Asks player the range of rows they want to select
     */
    public void askRowNumber(){
        if(numOfPlayers == 2){
            System.out.println("Enter row number 1 (you can select a number among 1 and 7):\n ");
            row1 = in.nextInt();
            do{
                System.out.println("You entered an invalid number :(" +
                        "Enter a number among 1 and 7");
                row1 = in.nextInt();
            }while (row1 < 1 || row1 > 7);

            System.out.println("Please enter row number 2");
            row2 = in.nextInt();
            do{
                System.out.println("You entered an invalid number :(" +
                        "Enter a number among 1 and 7");
                row2 = in.nextInt();
            }while (row2 < 1 || row2 > 7);
        }
        else if(numOfPlayers == 3 || numOfPlayers == 4){
            System.out.println("Enter row number 1 (you can select a number among 0 and 8):\n ");
            row1 = in.nextInt();
            do{
                System.out.println("You entered an invalid number :(" +
                        "Enter a number among 1 and 7");
                row1 = in.nextInt();
            }while (row1 < 0 || row1 > 8);

            System.out.println("Please enter row number 2");
            row2 = in.nextInt();
            do{
                System.out.println("You entered an invalid number :(" +
                        "Enter a number among 1 and 7");
                row2 = in.nextInt();
            }while (row2 < 0 || row2 > 8);
        }

    }

    public void drawGraphics() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println(graphics.getGraphics().render(new SimpleColorRenderer()));
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
        String[] args = null;
        if (tokens.length > 1) args = Arrays.copyOfRange(tokens, 1, tokens.length);
        return new Input(tokens[0], args);
    }

    private void parseInput(Input input) {
        switch (input.command()) {
            case "name" -> notifyObservers(o -> o.onChooseUsername(input.args[0]));
            case "new"  -> notifyObservers(o -> o.newGame(Integer.parseInt(input.args[0])));
            case "join" -> notifyObservers(ControllerObserver::joinGame);
            case "quit" -> notifyObservers(ControllerObserver::quitGame);
            case "load" -> notifyObservers(o -> o.loadSavedGame(input.args[0]));
            case "save" -> notifyObservers(ControllerObserver::saveCurrentGame);
            case "/m" -> notifyObservers(o -> o.onChatMessageSent(String.join(" ", input.args)));
            case "/w" -> notifyObservers(o -> o.onChatWhisperSent(String.join(" ", Arrays.copyOfRange(input.args, 1, input.args.length)), input.args[0]));
            case "pick" -> {
                int[] coords1, coords2;
                coords1 = parseBoardCoordinates(input.args[0]);
                coords2 = parseBoardCoordinates(input.args[1]);
                notifyObservers(o -> o.onChooseTilesOnBoard(coords1[0], coords1[1], coords2[0], coords2[1]));
            }
            case "drop" -> notifyObservers(o -> o.onChooseColumnOfBookshelf(Integer.parseInt(input.args[0])));
        }
    }

    private int[] parseBoardCoordinates(String input) {
        char rowChar = input.charAt(0);
        int row, col;

        row = rowChar - 'A';
        col = Integer.parseInt(input.substring(1));

        return new int[] {row, col};
    }
}
