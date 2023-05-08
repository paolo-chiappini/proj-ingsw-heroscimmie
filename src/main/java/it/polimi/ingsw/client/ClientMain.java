package it.polimi.ingsw.client;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.view.cli.CliView;

public abstract class ClientMain {
    public static void main(String[] args) {
        final String SERVER_ADDR = "localhost";
        new ClientController(new CliView(), SERVER_ADDR);
    }
}
