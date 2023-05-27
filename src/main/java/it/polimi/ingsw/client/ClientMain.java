package it.polimi.ingsw.client;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.view.cli.ViewCli;

public abstract class ClientMain {
    public static void main(View view, String[] args) {
        new ClientController(view, args[0]);
    }
}
