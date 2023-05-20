package it.polimi.ingsw.client.view;

public enum ViewMessage {
    CANNOT_PERFORM_ACTION_WHILE_IN_GAME("Cannot perform this action while in game"),
    CANNOT_PERFORM_ACTION_OUTSIDE_GAME("Cannot perform this action while NOT in a game"),
    UNABLE_TO_CONNECT_TO_SERVER("Unable to connect to server"),
    CONNECTION_TO_SERVER_LOST("Connection to server has been lost"),
    GAME_JOINED("Joined lobby, awaiting players..."),
    GAME_CREATED("Lobby has been successfully created");

    private final String defaultMessage;
    ViewMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
