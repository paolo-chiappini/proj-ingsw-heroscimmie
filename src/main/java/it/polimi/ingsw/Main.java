package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.view.cli.ViewCli;
import it.polimi.ingsw.client.view.gui.ViewGui;
import it.polimi.ingsw.server.ServerMain;

public class Main {
    private static final String SERVER_ARG = "--server";
    private static final String CLI_ARG = "--cli";
    private static final String GUI_ARG = "--gui";
    private static final String GUI_MENU_ARG = "--menu";
    private static final String GUI_BOARD_ARG = "--board";
    private static final String SERVER_ADDR = "--server-addr";

    public static void main(String[] args) {
        String startupMode = null;
        String serverAddress = null;
        String guiStartingView = null;

        int argIndex = 0;
        for (String arg : args) {
            if ((arg.equalsIgnoreCase(SERVER_ARG) ||
                    arg.equalsIgnoreCase(CLI_ARG) ||
                    arg.equalsIgnoreCase(GUI_ARG))) {

                if (startupMode != null) {
                    System.out.println("More than one startup mode specified, only one can be selected");
                    return;
                }

                startupMode = arg.toLowerCase();

            } else if (arg.equalsIgnoreCase(SERVER_ADDR)) {
                if (startupMode.equals(SERVER_ARG)) {
                    System.out.println("Cannot specify address for server startup mode, address will be assigned automatically");
                    return;
                }

                if (args.length <= argIndex + 1) {
                    System.out.println("Missing server address");
                    return;
                }

                serverAddress = args[argIndex + 1];
            } else if (arg.equalsIgnoreCase(GUI_MENU_ARG)||
                        arg.equalsIgnoreCase(GUI_BOARD_ARG)) {
                guiStartingView = arg.toLowerCase();
            }

            argIndex++;
        }

        if (startupMode == null) {
            System.out.println("Unable to start application, no startup mode provided. Please choose one between --server, --cli and --gui");
            return;
        }

        if (!startupMode.equals(SERVER_ARG) && serverAddress == null) {
            System.out.println("Unable to start client, no server address provided. Please use '--server-addr [address]'");
            return;
        }

        switch (startupMode) {
            case SERVER_ARG -> ServerMain.main(args);
            case CLI_ARG -> ClientMain.main(new ViewCli(), new String[] { serverAddress });
            case GUI_ARG -> ClientMain.main(new ViewGui(guiStartingView), new String[] { serverAddress });
        }
    }
}
