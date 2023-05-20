package it.polimi.ingsw.client;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.view.cli.ViewCli;

public abstract class ClientMain {
    public static void main(String[] args) {
        new ClientController(new ViewCli(), args[1]);
    }
}
