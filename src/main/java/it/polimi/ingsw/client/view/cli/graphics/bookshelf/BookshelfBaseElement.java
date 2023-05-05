package it.polimi.ingsw.client.view.cli.graphics.bookshelf;

import it.polimi.ingsw.client.view.cli.graphics.grids.CellElement;
import it.polimi.ingsw.client.view.cli.graphics.grids.TableChars;
import it.polimi.ingsw.client.view.cli.graphics.grids.GridElement;
import it.polimi.ingsw.client.view.cli.graphics.simple.CliElement;
import it.polimi.ingsw.client.view.cli.graphics.simple.CliTextElement;
import it.polimi.ingsw.client.view.cli.graphics.util.CliDrawer;
import it.polimi.ingsw.client.view.cli.graphics.util.ReplaceTarget;
import it.polimi.ingsw.client.view.cli.graphics.util.StringToCliTextConverter;

import java.util.List;

/**
 * Represents the base of a player's bookshelf.
 */
public class BookshelfBaseElement extends CliElement {
    public BookshelfBaseElement() {
        int bookshelfWidth = BookshelfElement.WIDTH;
        this.representation.add(generateTopRow(bookshelfWidth));
        this.representation.add(generateMiddleRow(bookshelfWidth));
        this.representation.add(generateBottomRow(bookshelfWidth));
        // add coordinates
        CliDrawer.superimposeElement(GridElement.generateCoordinatesRow(bookshelfWidth), this, 1, 1, ReplaceTarget.EMPTY);
    }

    /**
     * Generates the top row of a bookshelf's base.
     * @param cellsInRow number of cells in a row of the bookshelf.
     * @return the top row of a bookshelf's base.
     */
    private List<CliTextElement> generateTopRow(int cellsInRow) {
        String cell = TableChars.BOTTOM_T.getChar() +
                String.valueOf(TableChars.HORIZONTAL_BAR.getChar()).repeat(CellElement.WIDTH + GridElement.PADDING);
        StringBuilder stringBuilder = new StringBuilder(cell.repeat(cellsInRow));
        stringBuilder.insert(0, TableChars.TOP_LEFT_CORNER.getChar());
        stringBuilder.append(TableChars.BOTTOM_T.getChar())
                .append(TableChars.TOP_RIGHT_CORNER.getChar());
        return StringToCliTextConverter.convert(stringBuilder.toString());
    }

    /**
     * Generates the bottom row of a bookshelf's base.
     * @param cellsInRow number of cells in a row of the bookshelf.
     * @return the top bottom of a bookshelf's base.
     */
    private List<CliTextElement> generateBottomRow(int cellsInRow) {
        String cell = String.valueOf(TableChars.HORIZONTAL_BAR.getChar()).repeat(CellElement.WIDTH + GridElement.PADDING + 1);
        StringBuilder stringBuilder = new StringBuilder(cell.repeat(cellsInRow));
        stringBuilder.insert(0, TableChars.BOTTOM_LEFT_CORNER.getChar());
        stringBuilder.append(TableChars.HORIZONTAL_BAR.getChar())
                .append(TableChars.BOTTOM_RIGHT_CORNER.getChar());
        return StringToCliTextConverter.convert(stringBuilder.toString());
    }

    /**
     * Generates the central row of a bookshelf's base.
     * @param cellsInRow number of cells in a row of the bookshelf.
     * @return the central row of a bookshelf's base.
     */
    private List<CliTextElement> generateMiddleRow(int cellsInRow) {
        String cell = String.valueOf(TableChars.SPACE.getChar()).repeat(CellElement.WIDTH + GridElement.PADDING + 1);
        StringBuilder stringBuilder = new StringBuilder(cell.repeat(cellsInRow));
        stringBuilder.insert(0, TableChars.VERTICAL_BAR.getChar());
        stringBuilder.append(TableChars.SPACE.getChar())
                .append(TableChars.VERTICAL_BAR.getChar());
        return StringToCliTextConverter.convert(stringBuilder.toString());
    }
}
