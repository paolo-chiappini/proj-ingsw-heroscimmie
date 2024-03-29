package it.polimi.ingsw.client.view.cli.graphics.grids;

import it.polimi.ingsw.client.view.cli.graphics.simple.CliElement;
import it.polimi.ingsw.client.view.cli.graphics.simple.CliTextElement;
import it.polimi.ingsw.client.view.cli.graphics.util.CliDrawer;
import it.polimi.ingsw.client.view.cli.graphics.util.StringToCliTextConverter;

import java.util.List;

/**
 * Represents a generic small grid element that can contain other SmallGridCellElements.
 */
public abstract class SmallGridElement extends CliElement {
    protected SmallCellElement[][] contents;

    /**
     * Sets an element in the grid.
     *
     * @param element element to add to the grid.
     * @param x       x coordinate of the cell where to set the element.
     * @param y       y coordinate of the cell where to set the element.
     */
    public void setElement(SmallCellElement element, int x, int y) {
        contents[y][x] = element;
        int adaptedX, adaptedY;
        adaptedX = adaptXCoords(x);
        adaptedY = adaptYCoords(y);
        if (element == null) {
            CliDrawer.clearArea(
                    this,
                    adaptedX, adaptedY,
                    adaptedX + SmallCellElement.WIDTH - 1, adaptedY + SmallCellElement.HEIGHT - 1
            );
        } else {
            CliDrawer.superimposeElement(element,
                    this,
                    adaptedX, adaptedY);
        }
    }

    /**
     * @param row cell row
     * @param col cell column
     * @return the element contained at the specified coordinates
     */
    public SmallCellElement getElement(int row, int col) {
        return contents[row][col];
    }

    /**
     * Adapts the x coordinate to fit the grid representation.
     *
     * @param x coordinate to adapt.
     * @return the adapted x coordinate.
     */
    protected static int adaptXCoords(int x) {
        int cellWidth = SmallCellElement.WIDTH + 1;
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
        String cell = "|" + (" ").repeat(SmallCellElement.WIDTH);
        StringBuilder stringBuilder = new StringBuilder(cell.repeat(cellsInRow));
        stringBuilder.setCharAt(0, '[');
        stringBuilder.append(']');
        return StringToCliTextConverter.convert(stringBuilder.toString());
    }
}
