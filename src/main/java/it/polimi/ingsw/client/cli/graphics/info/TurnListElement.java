package it.polimi.ingsw.client.cli.graphics.info;

import it.polimi.ingsw.client.cli.graphics.grids.TableChars;
import it.polimi.ingsw.client.cli.graphics.simple.*;
import it.polimi.ingsw.client.cli.graphics.util.CliDrawer;
import it.polimi.ingsw.client.cli.graphics.util.ReplaceTarget;
import it.polimi.ingsw.exceptions.IllegalActionException;

import java.util.LinkedList;
import java.util.List;

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

    private final List<PlayerRecord> players;
    private int currTurnIndex;

    private class PlayerRecord {
        private final String username;
        private int score;
        private final boolean isClient;
        private boolean isDisconnected;

        public PlayerRecord(String username, int score, boolean isClient) {
            this.username = username;
            this.score = score;
            this.isClient = isClient;
            this.isDisconnected = false;
        }
        public void setConnectionStatus(boolean disconnected) { this.isDisconnected = disconnected; }
        public void setScore(int score) { this.score = score; }
        public String getUsername() { return username; }
        public int getScore() { return score; }
        public boolean isClient() { return isClient; }
        public boolean isDisconnected() { return isDisconnected; }
    }

    public TurnListElement() {
        super(WIDTH, HEIGHT);
        this.players = new LinkedList<>();

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

    public void setCurrTurnIndex(int currTurnIndex) {
        this.currTurnIndex = currTurnIndex;
        drawTurnIndex();
    }

    private void drawTurnIndex() {
        final String arrow = "->";
        final String blocked = "xx";

        CliDrawer.clearArea(this, 1, 3, 4, HEIGHT - 2);
        for (int i = 0; i < MAX_PLAYERS; i++) {
            String line = "  ";
            if (i >= players.size() || players.get(i).isDisconnected()) line = blocked;
            else if (i == currTurnIndex) line = arrow;
            CliDrawer.superimposeElement(
                    new RowElement(line + (i + 1) + "."),
                    this, 1, 3 + i
            );
        }
    }

    private void drawPlayerNames() {
        final String blocked = "-------";

        for (int i = 0; i < MAX_PLAYERS; i++) {
            String line = "";
            if (i >= players.size()) line = blocked;
            else {
                String username = players.get(i).getUsername();
                line = username.substring(0, Integer.min(username.length(), MAX_PLAYER_NAME_LENGTH));
            }
            CliDrawer.clearArea(this, 6, 3 + i, WIDTH - 9, 3 + i);
            CliDrawer.superimposeElement(new RowElement(line),this, 6, 3 + i);
        }
    }

    private void drawScores() {
        final String hidden = "~~~~";
        final String blocked = "xxxx";

        for (int i = 0; i < MAX_PLAYERS; i++) {
            String line;
            if (i >= players.size()) line = blocked;
            else if (players.get(i).isClient()) line = String.valueOf(players.get(i).getScore());
            else line = hidden;
            CliDrawer.clearArea(this, WIDTH - 6, 3 + i, WIDTH - 2, 3 + i);
            CliDrawer.superimposeElement(new RowElement(line),this, WIDTH - 6, 3 + i);
        }
    }

    public void addPlayer(String username, int score, boolean isClient) {
        if (players.size() == MAX_PLAYERS) throw new IllegalActionException("Maximum amount of players reached");
        players.add(new PlayerRecord(username, score, isClient));
        drawPlayerNames();
        drawScores();
    }

    public void updateConnectionStatus(String username, boolean isDisconnected) {
        players.stream()
                .filter(p -> p.getUsername().equals(username))
                .findFirst()
                .orElseThrow()
                .setConnectionStatus(isDisconnected);
        drawTurnIndex();
    }

    public void updateScores(int[] scores) {
        for (int i = 0; i < scores.length; i++) {
            players.get(i).setScore(scores[i]);
        }
        drawScores();
    }
}
