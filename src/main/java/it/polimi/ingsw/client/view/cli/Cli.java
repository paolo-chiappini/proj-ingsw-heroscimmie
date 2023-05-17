package it.polimi.ingsw.client.view.cli;

import java.io.PrintStream;
import java.util.Scanner;

public class Cli {
    private Scanner in = new Scanner(System.in);
    private PrintStream out = new PrintStream(System.out);
    private String nickname;
    private int numOfPlayers, col1, col2, row1, row2;

    public Cli(){
        this.init();
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
     * Asks to user how many players are going to join the match
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
     * Ask to player the range of column they want to select
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
     * Asks to player the range of rows they want to select
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


}
