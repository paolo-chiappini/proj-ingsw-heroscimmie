package it.polimi.ingsw.client.cli.graphics.util;

import it.polimi.ingsw.client.cli.graphics.board.BoardElement;
import it.polimi.ingsw.client.cli.graphics.board.BoardSpaceElement;
import it.polimi.ingsw.client.cli.graphics.board.BoardTileType;
import it.polimi.ingsw.client.cli.graphics.bookshelf.BookshelfBaseElement;
import it.polimi.ingsw.client.cli.graphics.bookshelf.BookshelfElement;
import it.polimi.ingsw.client.cli.graphics.bookshelf.SmallBookshelfElement;
import it.polimi.ingsw.client.cli.graphics.goals.common.CommonGoalCardElement;
import it.polimi.ingsw.client.cli.graphics.goals.personal.PersonalGoalCardElement;
import it.polimi.ingsw.client.cli.graphics.grids.TableChars;
import it.polimi.ingsw.client.cli.graphics.grids.TileGridElement;
import it.polimi.ingsw.client.cli.graphics.info.BonusesInfoElement;
import it.polimi.ingsw.client.cli.graphics.info.ChatElement;
import it.polimi.ingsw.client.cli.graphics.info.TurnListElement;
import it.polimi.ingsw.client.cli.graphics.simple.ColumnElement;
import it.polimi.ingsw.client.cli.graphics.simple.RectangleElement;
import it.polimi.ingsw.client.cli.graphics.simple.RowElement;
import it.polimi.ingsw.client.cli.graphics.tiles.SmallTileElement;
import it.polimi.ingsw.client.cli.graphics.tiles.TileElement;
import it.polimi.ingsw.server.model.tile.TileType;
import it.polimi.ingsw.util.FileIOManager;
import it.polimi.ingsw.util.FilePath;
import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class used to manage the drawing of the CLI graphics.
 */
public abstract class CliGraphicsManager {
    private static final String BOARD_TEMPLATE_FILE = "board_template.json";
    private static final BoardElement board = new BoardElement();
    private static final RowElement boardXCoordinates = TileGridElement.generateCoordinatesRow(9);
    private static final ColumnElement boardYCoordinates = TileGridElement.generateCoordinatesColumn(9);
    private static final BookshelfElement bookshelf = new BookshelfElement();
    private static final BookshelfBaseElement bookshelfBaseElement = new BookshelfBaseElement();
    private static final SmallBookshelfElement otherBookshelf1 = new SmallBookshelfElement();
    private static final SmallBookshelfElement otherBookshelf2 = new SmallBookshelfElement();
    private static final SmallBookshelfElement otherBookshelf3 = new SmallBookshelfElement();
    private static final RowElement horizontalBreak = new RowElement(String.valueOf(TableChars.HORIZONTAL_BAR.getChar()).repeat(22));
    private static final BonusesInfoElement bonusesInfoElement = new BonusesInfoElement();
    private static final RowElement bonusTableTitle = new RowElement("Bonus points");
    private static final RowElement smallBookshelfBase = new RowElement(("▓").repeat(otherBookshelf1.getWidth() + 2));
    private static final List<RowElement> otherBookshelvesHeaders = new ArrayList<>();
    private static final ChatElement chatElement = new ChatElement();
    private static final RowElement chatTitle = new RowElement("Chat");

    private static CommonGoalCardElement commonGoalCardElement1 = new CommonGoalCardElement("Common Goal 1", 0, 0);
    private static CommonGoalCardElement commonGoalCardElement2 = new CommonGoalCardElement("Common Goal 2", 0, 0);
    private static PersonalGoalCardElement personalGoalCardElement = new PersonalGoalCardElement("Personal Goal", 0);
    private static TurnListElement turnListElement = new TurnListElement(new String[]{}, new int[]{}, 0);

    private static final int boardXOffset = 1;
    private static final int boardYOffset = 1;
    private static final int boardXCoordsXOffset = 1;
    private static final int boardXCoordsYOffset = 0;
    private static final int boardYCoordsXOffset = 0;
    private static final int boardYCoordsYOffset = 0;
    private static final int bookshelfBaseXOffset = board.getWidth() + 1;
    private static final int bookshelfBaseYOffset = board.getHeight() - bookshelfBaseElement.getHeight() + 1;
    private static final int bookshelfXOffset = bookshelfBaseXOffset + 1;
    private static final int bookshelfYOffset = bookshelfBaseYOffset - bookshelf.getHeight() + 1;
    private static final int goalXOffset = bookshelfBaseXOffset + bookshelfBaseElement.getWidth() + 1;
    private static final int commonGoal1YOffset = 1;
    private static final int commonGoal2YOffset = commonGoal1YOffset + commonGoalCardElement1.getHeight() + 1;
    private static final int horizontalBreakYOffset = commonGoal2YOffset + commonGoalCardElement2.getHeight() + 1;
    private static final int personalGoalYOffset = horizontalBreakYOffset + 2;
    private static final int otherBookshelvesXOffset = goalXOffset + personalGoalCardElement.getWidth() + 2;
    private static final int otherBookshelf1YOffset = 1;    
    private static final int otherBookshelf2YOffset = otherBookshelf1YOffset + otherBookshelf1.getHeight() + 5;
    private static final int otherBookshelf3YOffset = otherBookshelf2YOffset + otherBookshelf2.getHeight() + 5;
    private static final int turnListYOffset = boardYOffset + board.getHeight() + 1;
    private static final int chatXOffset = boardXOffset + turnListElement.getWidth() + 2;

    private static final int hPadding = 6;
    private static final int totalWidth = board.getWidth() +
            bookshelfBaseElement.getWidth() +
            personalGoalCardElement.getWidth() +
            otherBookshelf1.getWidth() +
            hPadding;
    private static final int vPadding = 0;
    private static final int totalHeight = board.getHeight() +
            commonGoalCardElement1.getHeight() +
            vPadding;

    private static final RectangleElement mainPanel = new RectangleElement(totalWidth, totalHeight);



    /**
     * (Re)loads the graphic elements setting them on single text panel.
     */
    public static void reloadElements() {
        initBoard();

        CliDrawer.superimposeElement(board, mainPanel, boardXOffset, boardYOffset, ReplaceTarget.ALL);
        CliDrawer.superimposeElement(boardXCoordinates, mainPanel, boardXCoordsXOffset, boardXCoordsYOffset, ReplaceTarget.ALL);
        CliDrawer.superimposeElement(boardYCoordinates, mainPanel, boardYCoordsXOffset, boardYCoordsYOffset, ReplaceTarget.ALL);
        CliDrawer.superimposeElement(bookshelf, mainPanel, bookshelfXOffset, bookshelfYOffset, ReplaceTarget.ALL);
        CliDrawer.superimposeElement(bookshelfBaseElement, mainPanel, bookshelfBaseXOffset, bookshelfBaseYOffset, ReplaceTarget.ALL);
        CliDrawer.superimposeElement(bonusesInfoElement, mainPanel, bookshelfXOffset, boardYOffset, ReplaceTarget.ALL);
        CliDrawer.superimposeElement(bonusTableTitle, mainPanel, bookshelfXOffset + 1, boardXCoordsYOffset, ReplaceTarget.ALL);
        CliDrawer.superimposeElement(horizontalBreak, mainPanel, goalXOffset, horizontalBreakYOffset, ReplaceTarget.ALL);
        CliDrawer.superimposeElement(chatElement, mainPanel, chatXOffset, turnListYOffset, ReplaceTarget.ALL);
        CliDrawer.superimposeElement(chatTitle, mainPanel, chatXOffset + 1, turnListYOffset - 1, ReplaceTarget.ALL);
        CliDrawer.superimposeElement(commonGoalCardElement1, mainPanel, goalXOffset, commonGoal1YOffset, ReplaceTarget.ALL);
        CliDrawer.superimposeElement(commonGoalCardElement2, mainPanel, goalXOffset, commonGoal2YOffset, ReplaceTarget.ALL);
        CliDrawer.superimposeElement(personalGoalCardElement, mainPanel, goalXOffset, personalGoalYOffset, ReplaceTarget.ALL);
        CliDrawer.superimposeElement(turnListElement, mainPanel, boardXOffset, turnListYOffset, ReplaceTarget.ALL);

        var offsets = List.of(otherBookshelf1YOffset, otherBookshelf2YOffset, otherBookshelf3YOffset);
        var bookshelves = List.of(otherBookshelf1, otherBookshelf2, otherBookshelf3);
        for (int i = 0; i < otherBookshelvesHeaders.size(); i++) {
            CliDrawer.superimposeElement(otherBookshelvesHeaders.get(i), mainPanel, otherBookshelvesXOffset, offsets.get(i) - 1, ReplaceTarget.ALL);
            CliDrawer.superimposeElement(smallBookshelfBase, mainPanel, otherBookshelvesXOffset - 1, offsets.get(i) + otherBookshelf1.getHeight(), ReplaceTarget.ALL);
            CliDrawer.superimposeElement(bookshelves.get(i), mainPanel, otherBookshelvesXOffset, offsets.get(i), ReplaceTarget.ALL);
        }
    }

    private static void applyChanges() {
        reloadElements();
    }

    /**
     * Prints the CLI graphics to standard out.
     * @param iCliRenderer type of renderer.
     */
    public static void draw(ICliRenderer iCliRenderer) {
        System.out.println(mainPanel.render(iCliRenderer));
    }

    private static void initBoard() {
        var template = getBoardTemplate();
        for (int i = 0; i < template.length; i++) {
            for (int j = 0; j < template[i].length; j++) {
                if (template[i][j] != null) board.setElement(new BoardSpaceElement(template[i][j]), j, i);
            }
        }
    }

    private static BoardTileType[][] getBoardTemplate() {
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

    /**
     * @param playerOrdinal index of the player in order of turns.
     * @return the bookshelf of the player.
     */
    public static SmallBookshelfElement getOtherPlayerBookshelf(int playerOrdinal) {
        return List.of(otherBookshelf1, otherBookshelf2, otherBookshelf3).get(playerOrdinal - 1);
    }

    /**
     * Notifies the manager that the board has been updated.
     * @param updatedBoard contents of the board.
     */
    public static void notifyBoardChange(int[][] updatedBoard) {
        for (int i = 0; i < updatedBoard.length; i++) {
            for (int j = 0; j < updatedBoard[i].length; j++) {
                if (updatedBoard[i][j] < 0) continue;
                board.setElement(new TileElement(TileType.values()[updatedBoard[i][j]]), j, i);
            }
        }
        applyChanges();
    }

    /**
     * Notifies the manager that the main bookshelf of the player to
     * whom the graphics are shown has been updated.
     * @param updatedBookshelf contents of the bookshelf.
     */
    public static void notifyMainBookshelfChange(int[][] updatedBookshelf) {
        for (int i = 0; i < updatedBookshelf.length; i++) {
            for (int j = 0; j < updatedBookshelf[i].length; j++) {
                if (updatedBookshelf[i][j] < 0) continue;
                bookshelf.setElement(new TileElement(TileType.values()[updatedBookshelf[i][j]]), j, i);
            }
        }
        applyChanges();
    }

    /**
     * Notifies the manager that one of the other players'
     * bookshelf has been updated.
     * @param updatedBookshelf contents of the bookshelf.
     * @param playerOrdinal index of the player in order of turns.
     */
    public static void notifyOtherBookshelfChange(int[][] updatedBookshelf, int playerOrdinal) {
        var currBookshelf = getOtherPlayerBookshelf(playerOrdinal);
        for (int i = 0; i < updatedBookshelf.length; i++) {
            for (int j = 0; j < updatedBookshelf[i].length; j++) {
                if (updatedBookshelf[i][j] < 0) continue;
                currBookshelf.setElement(new SmallTileElement(TileType.values()[updatedBookshelf[i][j]]), j, i);
            }
        }
        applyChanges();
    }

    /**
     * Notifies the manager that the game has progressed
     * to the next turn.
     */
    public static void notifyNextTurn() {
        turnListElement.nextPlayer();
        applyChanges();
    }

    /**
     * Notifies the manager that the scores of the players have
     * been updated.
     * @param scores scores of the players.
     */
    public static void notifyScoreUpdate(int[] scores) {
        turnListElement.setScores(scores);
        applyChanges();
    }

    /**
     * Notifies the manager that a new chat message ad been received.
     * @param message contents of the message.
     * @param from player that sent the message.
     */
    public static void notifyNewChatMessage(String message, String from) {
        chatElement.addMessage(message, from, false);
        applyChanges();
    }

    /**
     * Notifies the manager that the player received a direct message.
     * @param message contents of the message.
     * @param from player that sent the message.
     */
    public static void notifyNewChatWhisper(String message, String from) {
        chatElement.addMessage(message, from, true);
        applyChanges();
    }

    /**
     * Sets the players in the game.
     * @param usernames players' names.
     * @param scores players' scores.
     * @param currPlayerIndex the index of the player being shown the graphics.
     */
    public static void setPlayers(String[] usernames, int[] scores, int currPlayerIndex) {
        turnListElement = new TurnListElement(usernames, scores, currPlayerIndex);

        for (int i = 0; i < usernames.length; i++) {
            if (i == currPlayerIndex) continue;
            otherBookshelvesHeaders.add(new RowElement(usernames[i]));
        }
        applyChanges();
    }

    /**
     * Sets a common goal card.
     * @param commonGoalIndex index of the card to set (0 or 1).
     * @param id id of the card to fetch from the deck.
     * @param points points shown on the card.
     */
    public static void setCommonGoalId(int commonGoalIndex, int id, int points) {
        if (commonGoalIndex == 0) commonGoalCardElement1 = new CommonGoalCardElement("Common Goal 1", id, points);
        else if (commonGoalIndex == 1) commonGoalCardElement2 = new CommonGoalCardElement("Common Goal 2", id, points);
        applyChanges();
    }

    /**
     * Sets the personal goal card.
     * @param id id of the card to fetch from the deck.
     */
    public static void setPersonalGoalId(int id) {
        personalGoalCardElement = new PersonalGoalCardElement("Personal Goal", id);
        applyChanges();
    }
}
