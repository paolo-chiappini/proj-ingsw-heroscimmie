package it.polimi.ingsw.client.view.cli.graphics.grids;

import it.polimi.ingsw.client.view.cli.graphics.simple.CliElement;
import it.polimi.ingsw.client.view.cli.graphics.simple.CliTextElement;
import it.polimi.ingsw.client.view.cli.graphics.simple.ColumnElement;
import it.polimi.ingsw.client.view.cli.graphics.simple.RowElement;
import it.polimi.ingsw.client.view.cli.graphics.util.CliDrawer;
import it.polimi.ingsw.client.view.cli.graphics.util.ReplaceTarget;
import it.polimi.ingsw.client.view.cli.graphics.util.StringToCliTextConverter;

import java.util.List;

/**
 * Represents a generic grid element that can contain other GridCellElements.
 */
public abstract class GridElement extends CliElement {
    public static final int PADDING = 2;
    protected CellElement[][] contents;

    /**
     * Sets an element in the grid.
     * @param element element to add to the grid.
     * @param x x coordinate of the cell where to set the element.
     * @param y y coordinate of the cell where to set the element.
     */
    public void setElement(CellElement element, int x, int y) {
        contents[y][x] = element;
        if (element == null) {
            CliDrawer.clearArea(this,
                    adaptXCoords(x), adaptYCoords(y),
                    adaptXCoords(x) + CellElement.WIDTH - 1, adaptYCoords(y) + CellElement.HEIGHT - 1);
        }
        else CliDrawer.superimposeElement(element, this, adaptXCoords(x), adaptYCoords(y), ReplaceTarget.ALL);
    }

    /**
     * Adapts the x coordinate to fit the grid representation.
     * @param x coordinate to adapt.
     * @return the adapted x coordinate.
     */
    protected static int adaptXCoords(int x) {
        int cellWidth = CellElement.WIDTH + PADDING + 1;
        return x * cellWidth + 2;
    }

    /**
     * Adapts the y coordinate to fit the grid representation.
     * @param y coordinate to adapt.
     * @return the adapted y coordinate.
     */
    protected static int adaptYCoords(int y) {
        int cellHeight = CellElement.HEIGHT + 1;
        return y * cellHeight + 1;
    }

    /**
     * Creates a row with numeric coordinates representing the
     * indexes of each column in the grid.
     * @param cellsInRow number of cells in a row.
     * @return the row of coordinates.
     */
    public static RowElement generateCoordinatesRow(int cellsInRow) {
        String cell = String.valueOf(TableChars.SPACE.getChar()).repeat((CellElement.WIDTH + PADDING + 1));
        StringBuilder stringBuilder = new StringBuilder(cell.repeat(cellsInRow));
        for (int i = 0; i < cellsInRow; i++) {
            stringBuilder.setCharAt((CellElement.WIDTH + PADDING + 1) * i + (CellElement.WIDTH + PADDING + 2) / 2, Character.forDigit(i + 1, 10));
        }
        return new RowElement(stringBuilder.toString());
    }

    /**
     * Creates a column with letter coordinates representing the
     * indexes of each row in the grid.
     * @param cellsInColumn number of cells in a column.
     * @return the column of coordinates.
     */
    public static ColumnElement generateCoordinatesColumn(int cellsInColumn) {
        String cell = String.valueOf(TableChars.SPACE.getChar()).repeat((CellElement.HEIGHT + 1));
        StringBuilder stringBuilder = new StringBuilder(cell.repeat(cellsInColumn));
        for (int i = 0; i < cellsInColumn; i++) {
            stringBuilder.setCharAt((CellElement.HEIGHT + 1) * i + (CellElement.HEIGHT + 2) / 2, (char)('A' + i));
        }
        return new ColumnElement(stringBuilder.toString());
    }

    /**
     * Creates a row representing the top of a table.
     * @param cellsInRow number of cells in a row.
     * @return the top row of a table.
     */
    public static List<CliTextElement> generateTopRow(int cellsInRow) {
        String cell = TableChars.TOP_T.getChar() +
                String.valueOf(TableChars.HORIZONTAL_BAR.getChar()).repeat(CellElement.WIDTH + PADDING);
        StringBuilder stringBuilder = new StringBuilder(cell.repeat(cellsInRow));
        stringBuilder.setCharAt(0, TableChars.TOP_LEFT_CORNER.getChar());
        stringBuilder.append(TableChars.TOP_RIGHT_CORNER.getChar());
        return StringToCliTextConverter.convert(stringBuilder.toString());
    }

    /**
     * Creates a row representing the cells of a table.
     * @param cellsInRow number of cells in a row.
     * @return the (empty) cells of a table.
     */
    public static List<CliTextElement> generateCellsRow(int cellsInRow) {
        String cell = TableChars.VERTICAL_BAR.getChar() +
                String.valueOf(TableChars.SPACE.getChar()).repeat(CellElement.WIDTH + PADDING);
        String temp = (cell.repeat(cellsInRow) + TableChars.VERTICAL_BAR.getChar());
        return StringToCliTextConverter.convert(temp);
    }

    /**
     * Creates a row representing the line between rows of a table.
     * @param cellsInRow number of cells in a row.
     * @return the line between rows of a table.
     */
    public static List<CliTextElement> generateMiddleRow(int cellsInRow) {
        String cell = TableChars.CROSS.getChar() +
                String.valueOf(TableChars.HORIZONTAL_BAR.getChar()).repeat(CellElement.WIDTH + PADDING);
        StringBuilder stringBuilder = new StringBuilder(cell.repeat(cellsInRow));
        stringBuilder.setCharAt(0, TableChars.LEFT_T.getChar());
        stringBuilder.append(TableChars.RIGHT_T.getChar());
        return StringToCliTextConverter.convert(stringBuilder.toString());
    }

    /**
     * Creates a row representing the bottom of a table.
     * @param cellsInRow number of cells in a row.
     * @return the bottom row of a table.
     */
    public static List<CliTextElement> generateBottomRow(int cellsInRow) {
        String cell = TableChars.BOTTOM_T.getChar() +
                String.valueOf(TableChars.HORIZONTAL_BAR.getChar()).repeat(CellElement.WIDTH + PADDING);
        StringBuilder stringBuilder = new StringBuilder(cell.repeat(cellsInRow));
        stringBuilder.setCharAt(0, TableChars.BOTTOM_LEFT_CORNER.getChar());
        stringBuilder.append(TableChars.BOTTOM_RIGHT_CORNER.getChar());
        return StringToCliTextConverter.convert(stringBuilder.toString());
    }
}
