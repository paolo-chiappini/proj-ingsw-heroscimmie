package it.polimi.ingsw.client.cli.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ViewCli {
    private InputStreamReader keyboardInput = new InputStreamReader(System.in);
    private BufferedReader reader = new BufferedReader(keyboardInput);

    private String nickname, gameType;
    private ArrayList<String> takenNicknames = new ArrayList<>();

    public ViewCli(/*List<String> playerNicknames, int numOfPlayers*/){
        /* System.out.println("MyShelfie game");
        System.out.println("Number of players:" + numOfPlayers);
        System.out.println("Nicknames: ");
        for(String p: playerNicknames)
            System.out.println(p + "\n");
        */
    }

    /**
     * When a player starts the game, he's asked to give a nickname, which cannot be taken or empty
     */
    public void askNickname() {
        while (takenNicknames.isEmpty() || isNicknameTaken(nickname)) {
            System.out.println("Please enter your nickname\n");
            try{ nickname = reader.readLine();}
            catch (IOException e){
                e.getMessage();
            }
            //notifyObserver(obs -> obs.onUpdateNickname(nickname));
        }
    }

    /**
     *  Asks how many players want to play
     */
    public void askPlayersNumber() {
        int playerNumber = 0;
        System.out.println("How many players are going to play? (You can choose among 2, 3 or 4 players): ");

        try {
            while(playerNumber < 2 || playerNumber >4)
                playerNumber = reader.read();
            //notifyObserver(obs -> obs.onUpdatePlayersNumber(playerNumber));
        } catch (IOException e) {
            e.getMessage();
        }
    }

    /**
     * Checks whether a nickname has already been taken by some player
     * @param n: nickname that needs to be checked
     * @return true if nickname n has already been taken
     * @return false if not
     */
    public boolean isNicknameTaken(String n){
        for(int i = 0; i < takenNicknames.size(); i++)
            if(n.equalsIgnoreCase(takenNicknames.get(i))){
                System.out.println("Nickname is already taken, please enter another one");
                return true;
            }
        return false;
    }

    public void askTypeOfGame() {
        System.out.println("New game? [N] \n Load game? [L] \n Join game?");
        try {
            gameType = reader.readLine();
        } catch (IOException e) {
            e.getMessage();
        }
        if (gameType.equalsIgnoreCase("n")) {
            System.out.println("You chose to create a new game");
            //create new game
            //notify observers
        } else if (gameType.equalsIgnoreCase("l")) {
            System.out.println("You chose to load a game");
            //load a game
            //notify observers
        } else if (gameType.equalsIgnoreCase("j")){
            System.out.println("You chose to join a game");
            //load a game
            //notify observers
        }
    }

}
