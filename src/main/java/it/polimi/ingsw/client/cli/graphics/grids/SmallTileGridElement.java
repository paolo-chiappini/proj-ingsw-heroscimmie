package it.polimi.ingsw.client.cli.graphics.grids;

import it.polimi.ingsw.client.cli.graphics.simple.CliElement;
import it.polimi.ingsw.client.cli.graphics.simple.CliTextElement;
import it.polimi.ingsw.client.cli.graphics.util.CliDrawer;
import it.polimi.ingsw.client.cli.graphics.util.ReplaceTarget;
import it.polimi.ingsw.client.cli.graphics.util.StringToCliTextConverter;

import java.util.List;

/**
 * Represents a generic small grid element that can contain other SmallGridCellElements.
 */
public abstract class SmallTileGridElement extends CliElement {
    protected SmallGridCellElement[][] contents;

    /**
     * Sets an element in the grid.
     * @param element element to add to the grid.
     * @param x x coordinate of the cell where to set the element.
     * @param y y coordinate of the cell where to set the element.
     */
    public void setElement(SmallGridCellElement element, int x, int y) {
        contents[y][x] = element;
        CliDrawer.superimposeElement(element, this, adaptXCoords(x), adaptYCoords(y), ReplaceTarget.ALL);
    }

    /**
     * Adapts the x coordinate to fit the grid representation.
     * @param x coordinate to adapt.
     * @return the adapted x coordinate.
     */
    protected static int adaptXCoords(int x) {
        int cellWidth = SmallGridCellElement.WIDTH + 1;
        return x * cellWidth + 1;
    }

    /**
     * Adapts the y coordinate to fit the grid representation.
     * @param y coordinate to adapt.
     * @return the adapted y coordinate.
     */
    protected static int adaptYCoords(int y) {
        return y;
    }

    /**
     * Creates a row representing the cells of a small table.
     * @param cellsInRow number of cells in a row.
     * @return the (empty) cells of a small table.
     */
    public static List<CliTextElement> generateRow(int cellsInRow) {
        String cell = "|" + (" ").repeat(SmallGridCellElement.WIDTH);
        StringBuilder stringBuilder = new StringBuilder(cell.repeat(cellsInRow));
        stringBuilder.setCharAt(0, '[');
        stringBuilder.append(']');
        return StringToCliTextConverter.convert(stringBuilder.toString());
    }
}
