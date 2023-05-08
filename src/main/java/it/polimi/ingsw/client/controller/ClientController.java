package it.polimi.ingsw.client;

import it.polimi.ingsw.client.virtualModel.ClientBoard;
import it.polimi.ingsw.client.virtualModel.ClientBookshelf;
import it.polimi.ingsw.client.virtualModel.ClientCommonGoalCard;
import it.polimi.ingsw.client.virtualModel.ClientPlayer;
import it.polimi.ingsw.server.messages.Message;
import it.polimi.ingsw.server.messages.MessageProvider;
import it.polimi.ingsw.server.messages.MessageType;
import it.polimi.ingsw.util.observer.ControllerObserver;

public class ClientController implements ControllerObserver {

    Client client;
    String username;
    private ClientBoard board = new ClientBoard();
    private ClientBookshelf bookshelf = new ClientBookshelf();
    private ClientPlayer player = new ClientPlayer(username);
    private ClientCommonGoalCard commonGoalCard = new ClientCommonGoalCard();
    private ViewCli view;

    public ClientController(ViewCli view){
        this.view = view;
        client.addObserver(this);
        view.addObserver(this);
    }

    /**
     *
     * @param data
     * @return
     */
    public Message createMessage(String data) {
        MessageProvider factory = new MessageProvider(MessageType.JSON);
        return factory.getInstanceForIncomingRequest(client.getSocket(),data);
    }

    /**
     * Notifies server whenever an username is updated
     * @param username
     */
    public void onChooseUsername(String username) {
        this.username = username;
        client.sendMessageToServer(createMessage("username:"+username));
    }

    /**
     * Notifies server whenever the number of players changes
     * @param playersNumber
     */
    public void onChoosePlayersNumber(int playersNumber) {
        client.sendMessageToServer(createMessage("number_of_players:"+ playersNumber));
    }

    /**
     * Notifies server whenever a player perform an action on their bookshelf
     * @param numberOfColumn: column on which the action is performed
     */
    public void onChooseColumnOfBookshelf(int numberOfColumn) {
        client.sendMessageToServer(createMessage("number_of_column:"+ numberOfColumn));
    }

    /**
     *
     * @param first
     * @param second
     * @param third
     */
    public void onChooseTilesOrder(int first, int second, int third) {
        client.sendMessageToServer(createMessage("tiles_order:"+ (first + second + third)));
    }

    /**
     * Notifies server when a group of tiles is picked up from the board
     * @param row1
     * @param col1
     * @param row2
     * @param col2
     */
    public void onChooseTilesOnBoard(int row1, int col1, int row2, int col2) {
        client.sendMessageToServer(createMessage("tiles_to_pick_up:"+ (row1 + col1 + row2 + col2)));
    }

    @Override
    public void update(Message message) {
        switch (message.getMethod())
        {
            case "BOARD" -> board.updateBoard(message.getBody());
            case "BOOKSHELF" -> bookshelf.updateBookshelf(message.getBody());
            case "PLAYER" -> player.updatePlayer(message.getBody());
            case "COMMON_GOAL_CARD" -> commonGoalCard.updateId(message.getBody());
            case "COMMON_GOAL_CARD_SCORE" -> commonGoalCard.updatePoints(message.getBody());
            case "USERNAME" -> player.updateUsername(message.getBody());
            case "PERSONAL_GOAL_CARD" -> player.updateId(message.getBody());
            case "SCORE" -> player.updateScore(message.getBody());
        }
    }
}
