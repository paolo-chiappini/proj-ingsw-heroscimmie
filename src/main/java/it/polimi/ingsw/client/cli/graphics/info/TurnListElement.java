package it.polimi.ingsw.client.cli.graphics.info;

import it.polimi.ingsw.client.cli.graphics.grids.TableChars;
import it.polimi.ingsw.client.cli.graphics.simple.*;
import it.polimi.ingsw.client.cli.graphics.util.CliDrawer;
import it.polimi.ingsw.client.cli.graphics.util.ReplaceTarget;

/**
 * Represents the list of players in the game, their scores
 * and the current player playing.
 */
public class TurnListElement extends FramedElement {
    private static final int HEIGHT = 8;
    private static final int WIDTH = 32;
    private static final int MAX_PLAYERS = 4;
    private static final int MAX_PLAYER_NAME_LENGTH = 18;
    private static final String template =
            """
            Turn Players            Scores
            """;

    private final String[] players;
    private int[] scores;
    private int currTurnIndex;
    private final int currPlayerIndex;

    /**
     * @param players players in the game.
     * @param scores scores of the players.
     * @param currPlayerIndex index of the player to whom the list is shown.
     */
    public TurnListElement(String[] players, int[] scores, int currPlayerIndex) {
        super(WIDTH, HEIGHT);
        this.players = players;
        this.scores = scores;
        this.currPlayerIndex = currPlayerIndex;

        var templateToElement = new JaggedElement(template);
        CliDrawer.superimposeElement(templateToElement, this, 1,1, ReplaceTarget.EMPTY);

        setBreaksAndCorners();

        drawTurnIndex();
        drawPlayerNames();
        drawScores();
    }

    private void setBreaksAndCorners() {
        // add horizontal break
        CliDrawer.superimposeElement(
                new RowElement(String.valueOf(TableChars.HORIZONTAL_BAR.getChar()).repeat(WIDTH)),
                this, 0, 2, ReplaceTarget.EMPTY
        );
        this.setCell(0, 2, new CliTextElement(TableChars.LEFT_T.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));
        this.setCell(WIDTH - 1, 2, new CliTextElement(TableChars.RIGHT_T.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));

        // add vertical breaks
        var verticalBreak = new ColumnElement(String.valueOf(TableChars.VERTICAL_BAR.getChar()).repeat(HEIGHT));
        CliDrawer.superimposeElement(verticalBreak, this, 5, 0, ReplaceTarget.EMPTY);
        CliDrawer.superimposeElement(verticalBreak, this, WIDTH - 8, 0, ReplaceTarget.EMPTY);

        // set corners
        this.setCell(5, 0, new CliTextElement(TableChars.TOP_T.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));
        this.setCell(WIDTH - 8, 0, new CliTextElement(TableChars.TOP_T.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));
        this.setCell(5, HEIGHT - 1, new CliTextElement(TableChars.BOTTOM_T.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));
        this.setCell(WIDTH - 8, HEIGHT - 1, new CliTextElement(TableChars.BOTTOM_T.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));
        this.setCell(5, 2, new CliTextElement(TableChars.CROSS.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));
        this.setCell(WIDTH - 8, 2, new CliTextElement(TableChars.CROSS.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));
    }

    public void nextPlayer() {
        currTurnIndex++;
        currTurnIndex = currTurnIndex % players.length;
        drawTurnIndex();
    }

    private void drawTurnIndex() {
        final String arrow = "->";
        final String blocked = "xx";

        CliDrawer.clearArea(this, 1, 3, 4, HEIGHT - 2);
        for (int i = 0; i < MAX_PLAYERS; i++) {
            String line = "  ";
            if (i >= players.length) line = blocked;
            else if (i == currTurnIndex) line = arrow;
            CliDrawer.superimposeElement(
                    new RowElement(line + (i + 1) + "."),
                    this, 1, 3 + i, ReplaceTarget.EMPTY
            );
        }
    }

    private void drawPlayerNames() {
        final String blocked = "-------";

        for (int i = 0; i < MAX_PLAYERS; i++) {
            String line = "";
            if (i >= players.length) line = blocked;
            else line = players[i].substring(0, Integer.min(players[i].length(), MAX_PLAYER_NAME_LENGTH));
            CliDrawer.superimposeElement(new RowElement(line),this, 6, 3 + i, ReplaceTarget.EMPTY);
        }
    }

    private void drawScores() {
        final String hidden = "~~~~";
        final String blocked = "xxxx";

        for (int i = 0; i < MAX_PLAYERS; i++) {
            String line = "";
            if (i >= players.length) line = blocked;
            else if (i == currPlayerIndex) line = String.valueOf(scores[i]);
            else line = hidden;
            CliDrawer.superimposeElement(new RowElement(line),this, WIDTH - 6, 3 + i, ReplaceTarget.EMPTY);
        }
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }
}
