package it.polimi.ingsw.client.view.cli.graphics.simple;

import it.polimi.ingsw.client.view.cli.graphics.grids.TableChars;
import it.polimi.ingsw.client.view.cli.graphics.util.CliDrawer;
import it.polimi.ingsw.client.view.cli.graphics.util.ReplaceTarget;

/**
 * Represents a rectangular element with a frame.
 */
public class FramedElement extends RectangleElement {
    public FramedElement(int width, int height) {
        super(width, height);
        addBorder(this);
        setCorners(this);
    }

    protected static void addBorder(CliElement element) {
        var horizontal = new RowElement(String.valueOf(TableChars.HORIZONTAL_BAR.getChar()).repeat(element.getWidth()));
        var vertical = new ColumnElement(String.valueOf(TableChars.VERTICAL_BAR.getChar()).repeat(element.getHeight()));

        // top border
        CliDrawer.superimposeElement(horizontal, element, 0, 0, ReplaceTarget.EMPTY);
        // bottom border
        CliDrawer.superimposeElement(horizontal, element, 0, element.getHeight() - 1, ReplaceTarget.EMPTY);
        // left border
        CliDrawer.superimposeElement(vertical, element, 0, 0, ReplaceTarget.EMPTY);
        // right border
        CliDrawer.superimposeElement(vertical, element, element.getWidth() - 1, 0, ReplaceTarget.EMPTY);
    }

    protected static void setCorners(CliElement element) {
        // top left corner
        element.setCell(0, 0, new CliTextElement(TableChars.TOP_LEFT_CORNER.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));
        // top right corner
        element.setCell(element.getWidth() - 1, 0, new CliTextElement(TableChars.TOP_RIGHT_CORNER.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));
        // bottom left corner
        element.setCell(0, element.getHeight() - 1, new CliTextElement(TableChars.BOTTOM_LEFT_CORNER.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));
        // bottom right corner
        element.setCell(element.getWidth() - 1, element.getHeight() - 1, new CliTextElement(TableChars.BOTTOM_RIGHT_CORNER.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));
    }
}
