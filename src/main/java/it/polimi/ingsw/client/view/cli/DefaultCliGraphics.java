package it.polimi.ingsw.client.view.cli;

import it.polimi.ingsw.client.cli.graphics.board.BoardElement;
import it.polimi.ingsw.client.cli.graphics.board.BoardSpaceElement;
import it.polimi.ingsw.client.cli.graphics.board.BoardTileType;
import it.polimi.ingsw.client.cli.graphics.bookshelf.BookshelfBaseElement;
import it.polimi.ingsw.client.cli.graphics.bookshelf.BookshelfElement;
import it.polimi.ingsw.client.cli.graphics.bookshelf.SmallBookshelfElement;
import it.polimi.ingsw.client.cli.graphics.goals.common.CommonGoalCardElement;
import it.polimi.ingsw.client.cli.graphics.goals.personal.PersonalGoalCardElement;
import it.polimi.ingsw.client.cli.graphics.grids.GridElement;
import it.polimi.ingsw.client.cli.graphics.grids.TableChars;
import it.polimi.ingsw.client.cli.graphics.info.BonusesInfoElement;
import it.polimi.ingsw.client.cli.graphics.info.ChatElement;
import it.polimi.ingsw.client.cli.graphics.info.TurnListElement;
import it.polimi.ingsw.client.cli.graphics.simple.CliElement;
import it.polimi.ingsw.client.cli.graphics.simple.ColumnElement;
import it.polimi.ingsw.client.cli.graphics.simple.RectangleElement;
import it.polimi.ingsw.client.cli.graphics.simple.RowElement;
import it.polimi.ingsw.client.cli.graphics.tiles.SmallTileElement;
import it.polimi.ingsw.client.cli.graphics.tiles.TileElement;
import it.polimi.ingsw.client.cli.graphics.util.CliDrawer;
import it.polimi.ingsw.server.model.tile.TileType;
import it.polimi.ingsw.util.FileIOManager;
import it.polimi.ingsw.util.FilePath;
import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DefaultCliGraphics {
    private final BoardElement boardElement;
    private final BookshelfElement mainBookshelf;
    private final BonusesInfoElement bonusesInfoElement;
    private final ChatElement chatElement;
    private final TurnListElement turnListElement;
    private CommonGoalCardElement commonGoalElement1;
    private CommonGoalCardElement commonGoalElement2;
    private PersonalGoalCardElement personalGoalCardElement;
    private final RowElement columnCoordinatesElement;
    private final ColumnElement rowCoordinatesElement;
    private final BookshelfBaseElement bookshelfBaseElement;

    private final List<String> players;
    private int clientIndex;
    private final HashMap<String, SmallBookshelfElement> otherBookshelves;

    private final RectangleElement mainPanel;

    public DefaultCliGraphics() {
        otherBookshelves = new HashMap<>();
        players = new LinkedList<>();
        clientIndex = 0;

        boardElement = new BoardElement();
        initBoard();

        mainBookshelf = new BookshelfElement();

        bonusesInfoElement = new BonusesInfoElement();
        chatElement = new ChatElement();
        turnListElement = new TurnListElement();

        commonGoalElement1 = new CommonGoalCardElement("", 0, 0);
        commonGoalElement2 = new CommonGoalCardElement("", 0, 0);

        personalGoalCardElement = new PersonalGoalCardElement("", 0);

        columnCoordinatesElement = GridElement.generateCoordinatesRow(9);
        rowCoordinatesElement = GridElement.generateCoordinatesColumn(9);
        bookshelfBaseElement = new BookshelfBaseElement();

        mainPanel = new RectangleElement(DefaultLayout.PANEL_WIDTH, DefaultLayout.PANEL_HEIGHT);
        initPanel();
    }

    private void initPanel() {
        addBoardToPanel();
        addBookshelfToPanel();
        addBonusesInfoToPanel();
        addTurnsInfoToPanel();
        addCommonGoalToPanel(0);
        addCommonGoalToPanel(1);
        addPersonalGoalToPanel();
        addChatToPanel();
        addElementToPanel(columnCoordinatesElement, DefaultLayout.COL_COORDINATES_X, DefaultLayout.COL_COORDINATES_Y);
        addElementToPanel(rowCoordinatesElement, DefaultLayout.ROW_COORDINATES_X, DefaultLayout.ROW_COORDINATES_Y);
        addElementToPanel(bookshelfBaseElement, DefaultLayout.BOOKSHELF_BASE_X, DefaultLayout.BOOKSHELF_BASE_Y);
        addElementToPanel(
                new RowElement(String.valueOf(TableChars.HORIZONTAL_BAR.getChar()).repeat(commonGoalElement1.getWidth())),
                DefaultLayout.HORIZONTAL_BREAK_X, DefaultLayout.HORIZONTAL_BREAK_Y
        );
        addElementToPanel(new RowElement("Bonus points"), DefaultLayout.BONUSES_X + 1, DefaultLayout.BONUSES_Y - 1);
        addElementToPanel(new RowElement("Chat"), DefaultLayout.CHAT_X + 1, DefaultLayout.CHAT_Y - 1);
    }

    private void addElementToPanel(CliElement element, int x, int y) {
        CliDrawer.superimposeElement(element, mainPanel, x, y);
    }

    private void addBoardToPanel() {
        addElementToPanel(boardElement, DefaultLayout.BOARD_X, DefaultLayout.BOARD_Y);
    }

    private void addBookshelfToPanel() {
        addElementToPanel(mainBookshelf, DefaultLayout.BOOKSHELF_X, DefaultLayout.BOOKSHELF_Y);
    }

    private void addBonusesInfoToPanel() {
        addElementToPanel(bonusesInfoElement, DefaultLayout.BONUSES_X, DefaultLayout.BONUSES_Y);
    }

    private void addTurnsInfoToPanel() {
        addElementToPanel(turnListElement, DefaultLayout.PLAYERS_X, DefaultLayout.PLAYERS_Y);
    }

    private void addCommonGoalToPanel(int index) {
        addElementToPanel(
                List.of(commonGoalElement1, commonGoalElement2).get(index),
                List.of(DefaultLayout.COMMON_GOAL_1_X, DefaultLayout.COMMON_GOAL_2_X).get(index),
                List.of(DefaultLayout.COMMON_GOAL_1_Y, DefaultLayout.COMMON_GOAL_2_Y).get(index));
    }

    private void addPersonalGoalToPanel() {
        addElementToPanel(personalGoalCardElement, DefaultLayout.PERSONAL_GOAL_X, DefaultLayout.PERSONAL_GOAL_Y);
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
        addElementToPanel(new RowElement(player), DefaultLayout.OTHER_BOOKSHELVES_X,
                DefaultLayout.OTHER_BOOKSHELVES_Y + DefaultLayout.OTHER_BOOKSHELVES_Y_OFFSET * index - 1);
        addElementToPanel(new RowElement(String.valueOf('▓').repeat(temp.getWidth() + 2)),
                DefaultLayout.OTHER_BOOKSHELVES_X - 1,
                DefaultLayout.OTHER_BOOKSHELVES_Y + DefaultLayout.OTHER_BOOKSHELVES_Y_OFFSET * index + temp.getHeight());
    }

    public CliElement getGraphics() {
        return this.mainPanel;
    }

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

    public void updateBookshelf(String player, int[][] update) {
        if (players.indexOf(player) == clientIndex) updateMainBookshelf(update);
        else updateOtherBookshelf(player, update);
    }

    public void updateBoard(int[][] update) {
        updateGrid(boardElement, update);
        addBoardToPanel();
    }

    private void updateMainBookshelf(int[][] update) {
        updateGrid(mainBookshelf, update);
        addBookshelfToPanel();
    }

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
    }

    public void updatePlayerConnectionStatus(String player, boolean isDisconnected) {
        turnListElement.updateConnectionStatus(player, isDisconnected);
        addTurnsInfoToPanel();
    }

    public void updateCommonGoalPoints(int cardIndex, int points) {
        List.of(commonGoalElement1, commonGoalElement2).get(cardIndex).setPoints(points);
        addCommonGoalToPanel(cardIndex);
    }

    public void addMessage(String message, String sender, boolean isWhisper) {
        chatElement.addMessage(message, sender, isWhisper);
        addChatToPanel();
    }

    public void setCommonGoal(int cardIndex, int id, int points) {
        switch (cardIndex) {
            case 0 -> commonGoalElement1 = new CommonGoalCardElement("Common Goal 1", id, points);
            case 1 -> commonGoalElement2 = new CommonGoalCardElement("Common Goal 2", id, points);
        }
        addCommonGoalToPanel(cardIndex);
    }

    public void setPersonalGoal(int id) {
        personalGoalCardElement = new PersonalGoalCardElement("Personal Goal", id);
        addPersonalGoalToPanel();
    }

    public void setCurrentTurn(int turn) {
        turnListElement.setCurrTurnIndex(turn);
        addTurnsInfoToPanel();
    }

    public void updatePlayerScore(String player, int score) {
        turnListElement.updatePlayerScore(player, score);
        addTurnsInfoToPanel();
    }

    private void initBoard() {
        var template = getBoardTemplate();
        for (int i = 0; i < template.length; i++) {
            for (int j = 0; j < template[i].length; j++) {
                if (template[i][j] != null) boardElement.setElement(new BoardSpaceElement(template[i][j]), j, i);
            }
        }
    }

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
