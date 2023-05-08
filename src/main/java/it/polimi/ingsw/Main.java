package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.server.ServerMain;

public class Main {
    public static void main(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case "--cli" -> {
                    ClientMain.main(args);
                    return;
                }
                /*case "--gui" -> {
                    ClientMain.main(args);
                    return;
                }*/
                case "--server" -> {
                    ServerMain.main(args);
                    return;
                }
                default -> {}
            }
        }
    }
}
