package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.view.cli.graphics.board.BoardElement;
import it.polimi.ingsw.client.view.cli.graphics.board.BoardSpaceElement;
import it.polimi.ingsw.client.view.cli.graphics.board.BoardTileType;
import it.polimi.ingsw.client.view.cli.graphics.bookshelf.BookshelfBaseElement;
import it.polimi.ingsw.client.view.cli.graphics.bookshelf.BookshelfElement;
import it.polimi.ingsw.client.view.cli.graphics.bookshelf.SmallBookshelfElement;
import it.polimi.ingsw.client.view.cli.graphics.goals.common.CommonGoalCardElement;
import it.polimi.ingsw.client.view.cli.graphics.goals.personal.PersonalGoalCardElement;
import it.polimi.ingsw.client.view.cli.graphics.grids.GridElement;
import it.polimi.ingsw.client.view.cli.graphics.grids.TableChars;
import it.polimi.ingsw.client.view.cli.graphics.info.BonusesInfoElement;
import it.polimi.ingsw.client.view.cli.graphics.info.ChatElement;
import it.polimi.ingsw.client.view.cli.graphics.info.TurnListElement;
import it.polimi.ingsw.client.view.cli.graphics.simple.CliElement;
import it.polimi.ingsw.client.view.cli.graphics.simple.ColumnElement;
import it.polimi.ingsw.client.view.cli.graphics.simple.RectangleElement;
import it.polimi.ingsw.client.view.cli.graphics.simple.RowElement;
import it.polimi.ingsw.client.view.cli.graphics.tiles.SmallTileElement;
import it.polimi.ingsw.client.view.cli.graphics.tiles.TileElement;
import it.polimi.ingsw.client.view.cli.graphics.util.CliDrawer;
import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.tile.TileType;
import it.polimi.ingsw.util.FileIOManager;
import it.polimi.ingsw.util.FilePath;
import it.polimi.ingsw.util.observer.ViewObserver;
import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Represents the "default" CLI graphic view.
 * The class contains both setup and update methods for the
 * various CLI graphic elements.
 */
public class DefaultCliGraphics implements ViewObserver {
    private final BoardElement boardElement;
    private final BookshelfElement mainBookshelf;
    private final BonusesInfoElement bonusesInfoElement;
    private final ChatElement chatElement;
    private final TurnListElement turnListElement;
    private PersonalGoalCardElement personalGoalCardElement;
    private final RowElement columnCoordinatesElement;
    private final ColumnElement rowCoordinatesElement;
    private final BookshelfBaseElement bookshelfBaseElement;

    private final List<String> players;
    private int clientIndex;
    private final HashMap<String, SmallBookshelfElement> otherBookshelves;
    private final List<Map.Entry<Integer, CommonGoalCardElement>> commonGoalCardElements;

    private BoardTileType[][] boardTemplate;

    private final RectangleElement mainPanel;

    public DefaultCliGraphics() {

        otherBookshelves = new HashMap<>();
        commonGoalCardElements = new LinkedList<>();
        players = new LinkedList<>();
        boardTemplate = null;
        clientIndex = 0;

        boardElement = new BoardElement();
        initBoard();

        mainBookshelf = new BookshelfElement();

        bonusesInfoElement = new BonusesInfoElement();
        chatElement = new ChatElement();
        turnListElement = new TurnListElement();

        personalGoalCardElement = new PersonalGoalCardElement("", 0);

        columnCoordinatesElement = GridElement.generateCoordinatesRow(9);
        rowCoordinatesElement = GridElement.generateCoordinatesColumn(9);
        bookshelfBaseElement = new BookshelfBaseElement();

        mainPanel = new RectangleElement(DefaultLayout.PANEL_WIDTH, DefaultLayout.PANEL_HEIGHT);
        initPanel();
    }

    /**
     * Initializes the main panel object.
     * The panel acts as a container for other elements.
     */
    private void initPanel() {
        addBoardToPanel();
        addBookshelfToPanel();
        addBonusesInfoToPanel();
        addTurnsInfoToPanel();
        addCommonGoalsToPanel();
        addPersonalGoalToPanel();
        addChatToPanel();

        // Board coordinates
        addElementToPanel(columnCoordinatesElement, DefaultLayout.COL_COORDINATES_X, DefaultLayout.COL_COORDINATES_Y);
        addElementToPanel(rowCoordinatesElement, DefaultLayout.ROW_COORDINATES_X, DefaultLayout.ROW_COORDINATES_Y);

        // Horizontal break line
        addElementToPanel(
                new RowElement(String.valueOf(TableChars.HORIZONTAL_BAR.getChar()).repeat(new CommonGoalCardElement("", 0, 0).getWidth())),
                DefaultLayout.GOAL_CARDS_X, DefaultLayout.HORIZONTAL_BREAK_Y
        );

        // Titles
        addElementToPanel(new RowElement("Bonus points"), DefaultLayout.BONUSES_X + 1, DefaultLayout.BONUSES_Y - 1);
        addElementToPanel(new RowElement("Chat"), DefaultLayout.CHAT_X + 1, DefaultLayout.CHAT_Y - 1);
    }

    /**
     * Adds a generic cli element to the panel.
     * @param element element to add.
     * @param x x coordinate.
     * @param y y coordinate.
     */
    private void addElementToPanel(CliElement element, int x, int y) {
        CliDrawer.superimposeElement(element, mainPanel, x, y);
    }

    private void addBoardToPanel() {
        addElementToPanel(boardElement, DefaultLayout.BOARD_X, DefaultLayout.BOARD_Y);
    }

    private void addBookshelfToPanel() {
        addElementToPanel(mainBookshelf, DefaultLayout.BOOKSHELF_X, DefaultLayout.BOOKSHELF_Y);
        // Bookshelf base (bookshelf coordinates)
        addElementToPanel(bookshelfBaseElement, DefaultLayout.BOOKSHELF_BASE_X, DefaultLayout.BOOKSHELF_BASE_Y);
    }

    private void addBonusesInfoToPanel() {
        addElementToPanel(bonusesInfoElement, DefaultLayout.BONUSES_X, DefaultLayout.BONUSES_Y);
    }

    private void addTurnsInfoToPanel() {
        addElementToPanel(turnListElement, DefaultLayout.PLAYERS_X, DefaultLayout.PLAYERS_Y);
    }

    private void addCommonGoalsToPanel() {
        for (int i = 0; i < commonGoalCardElements.size(); i++) {
            addElementToPanel(commonGoalCardElements.get(i).getValue(),
                    DefaultLayout.GOAL_CARDS_X,
                    DefaultLayout.COMMON_GOAL_1_Y + i * DefaultLayout.COMMON_GOALS_Y_OFFSET);
        }
    }

    private void addPersonalGoalToPanel() {
        addElementToPanel(personalGoalCardElement, DefaultLayout.GOAL_CARDS_X, DefaultLayout.PERSONAL_GOAL_Y);
    }

    private void addChatToPanel() {
        addElementToPanel(chatElement, DefaultLayout.CHAT_X, DefaultLayout.CHAT_Y);
    }

    private void addSmallBookshelfToPanel(String player) {
        var temp = new SmallBookshelfElement();
        int index = players.indexOf(player);
        index -= index > clientIndex ? 1 : 0;

        addElementToPanel(
                otherBookshelves.get(player),
                DefaultLayout.OTHER_BOOKSHELVES_X,
                DefaultLayout.OTHER_BOOKSHELVES_Y + DefaultLayout.OTHER_BOOKSHELVES_Y_OFFSET * index);

        // Name of the owner of the bookshelf
        addElementToPanel(new RowElement(player), DefaultLayout.OTHER_BOOKSHELVES_X,
                DefaultLayout.OTHER_BOOKSHELVES_Y + DefaultLayout.OTHER_BOOKSHELVES_Y_OFFSET * index - 1);

        // Small bookshelf base
        addElementToPanel(new RowElement(String.valueOf('▓').repeat(temp.getWidth() + 2)),
                DefaultLayout.OTHER_BOOKSHELVES_X - 1,
                DefaultLayout.OTHER_BOOKSHELVES_Y + DefaultLayout.OTHER_BOOKSHELVES_Y_OFFSET * index + temp.getHeight());
    }

    /**
     * Gets the main container element that can be rendered.
     * @return the main container element.
     */
    public CliElement getGraphics() {
        return this.mainPanel;
    }

    /**
     * Adds a new player to the list of players.
     * @param username player's name.
     * @param score player's score.
     * @param isClient true if the player is the one viewing the CLI.
     */
    public void addPlayer(String username, int score, boolean isClient) {
        players.add(username);
        if (isClient) clientIndex = players.indexOf(username);
        else {
            otherBookshelves.put(username, new SmallBookshelfElement());
            addSmallBookshelfToPanel(username);
        }
        turnListElement.addPlayer(username, score, isClient);
        addTurnsInfoToPanel();
    }

    /**
     * Updates the player's bookshelf.
     * @param player owner of the bookshelf.
     * @param update updated state of the bookshelf.
     */
    @Override
    public void updateBookshelf(String player, int[][] update) {
        if (players.indexOf(player) == clientIndex) updateMainBookshelf(update);
        else updateOtherBookshelf(player, update);
    }

    /**
     * Updates the main game board.
     * @param update updated state of the board.
     */
    @Override
    public void updateBoard(int[][] update) {
        // Restore board base
        initBoard();

        updateGrid(boardElement, update);
        addBoardToPanel();
    }

    // Updates the client's bookshelf.
    private void updateMainBookshelf(int[][] update) {
        updateGrid(mainBookshelf, update);
        addBookshelfToPanel();
    }

    // Updates one of the smaller bookshelves belonging to other players.
    private void updateOtherBookshelf(String owner, int[][] update) {
        var currBookshelf = otherBookshelves.get(owner);
        for (int i = 0; i < update.length; i++)
            for (int j = 0; j < update[i].length; j++)
                if (update[i][j] >= 0) currBookshelf.setElement(new SmallTileElement(TileType.values()[update[i][j]]), j, i);
        addSmallBookshelfToPanel(owner);
    }

    private void updateGrid(GridElement grid, int[][] update) {
        for (int i = 0; i < update.length; i++)
            for (int j = 0; j < update[i].length; j++)
                if (update[i][j] >= 0) grid.setElement(new TileElement(TileType.values()[update[i][j]]), j, i);
                else if (update[i][j] < 0 && !(grid.getElement(i, j) instanceof BoardSpaceElement))
                    grid.setElement(null, j, i);
    }

    /**
     * Updates the connection status of a player.
     * @param player username of the player to update.
     * @param isDisconnected true if the player has disconnected from the game.
     */
    @Override
    public void updatePlayerConnectionStatus(String player, boolean isDisconnected) {
        turnListElement.updateConnectionStatus(player, isDisconnected);
        addTurnsInfoToPanel();
    }

    /**
     * Updates the points on the specified common goal card.
     * @param cardId id of the card to update.
     * @param points updated points.
     */
    @Override
    public void updateCommonGoalPoints(int cardId, int points) {
        commonGoalCardElements.stream()
                .filter(pair -> pair.getKey() == cardId)
                .findFirst()
                .orElseThrow()
                .getValue()
                .setPoints(points);
        addCommonGoalsToPanel();
    }

    /**
     * Adds a new message to the chat.
     * @param message message to add.
     * @param sender username of the player who sent the message.
     * @param isWhisper true if the message is directed to the client,
     *                  false if it's a broadcast message.
     */
    public void addMessage(String message, String sender, boolean isWhisper) {
        chatElement.addMessage(message, sender, isWhisper);
        addChatToPanel();
    }

    /**
     * Sets a new common goal card.
     * (maximum number of common goals is 2)
     * @param id id of the card to set.
     * @param points points shown on the card.
     */
    public void setCommonGoal(int id, int points) {
        final int MAX_COMMON_GOALS = 2;
        if (commonGoalCardElements.size() == MAX_COMMON_GOALS) {
            throw new IllegalActionException("Maximum number of common goal cards already reached");
        }

        var newCardElement = new CommonGoalCardElement("Common Goal " + (commonGoalCardElements.size() + 1), id, points);
        commonGoalCardElements.add(Map.entry(id, newCardElement));
        addCommonGoalsToPanel();
    }

    /**
     * Sets the personal goal card.
     * @param id id of the card.
     */
    public void setPersonalGoal(int id) {
        personalGoalCardElement = new PersonalGoalCardElement("Personal Goal", id);
        addPersonalGoalToPanel();
    }

    /**
     * Sets the current turn in the turn list element.
     * @param turn current turn index.
     */
    public void setCurrentTurn(int turn) {
        turnListElement.setCurrTurnIndex(turn);
        addTurnsInfoToPanel();
    }

    /**
     * Updates the score of the specified player.
     * @param player username of the player to update.
     * @param score updated score.
     */
    @Override
    public void updatePlayerScore(String player, int score) {
        turnListElement.updatePlayerScore(player, score);
        addTurnsInfoToPanel();
    }

    /**
     * Updates the status of the game.
     * If the game is over, shows all players' scores.
     * @param isGameOver true if the game is over.
     */
    @Override
    public void updateGameStatus(boolean isGameOver) {
        turnListElement.updateGameState(isGameOver);
        addTurnsInfoToPanel();
    }

    // Sets the base of the board.
    private void initBoard() {
        if (boardTemplate == null) boardTemplate = getBoardTemplate();
        for (int i = 0; i < boardTemplate.length; i++) {
            for (int j = 0; j < boardTemplate[i].length; j++) {
                if (boardTemplate[i][j] != null)
                    boardElement.setElement(new BoardSpaceElement(boardTemplate[i][j]), j, i);
            }
        }
    }

    // Fetches the board template from file
    private BoardTileType[][] getBoardTemplate() {
        final String BOARD_TEMPLATE_FILE = "board_template.json";
        String json;
        try {
            json = FileIOManager.readFromFile(BOARD_TEMPLATE_FILE, FilePath.TEMPLATES);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        JSONArray array = new JSONArray(json);
        BoardTileType[][] template = new BoardTileType[9][9];
        for (int i = 0; i < array.length(); i++) {
            for (int j = 0; j < array.getJSONArray(i).length(); j++) {
                if (array.getJSONArray(i).getInt(j) - 3 < 0) template[i][j] = null;
                else template[i][j] = BoardTileType.values()[array.getJSONArray(i).getInt(j) - 3];
            }
        }
        return template;
    }
}
