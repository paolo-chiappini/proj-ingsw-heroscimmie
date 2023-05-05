package it.polimi.ingsw.client.view.cli.graphics.grids;

/**
 * ASCII characters used to build tables.
 */
public enum TableChars {
    TOP_LEFT_CORNER('┌'),
    TOP_RIGHT_CORNER('┐'),
    BOTTOM_LEFT_CORNER('└'),
    BOTTOM_RIGHT_CORNER('┘'),
    TOP_T('┬'),
    BOTTOM_T('┴'),
    LEFT_T('├'),
    RIGHT_T('┤'),
    VERTICAL_BAR('│'),
    HORIZONTAL_BAR('─'),
    CROSS('┼'),
    SPACE(' ');
    private final char c;
    TableChars(char c) { this.c = c;}
    public char getChar() { return c; }
}
