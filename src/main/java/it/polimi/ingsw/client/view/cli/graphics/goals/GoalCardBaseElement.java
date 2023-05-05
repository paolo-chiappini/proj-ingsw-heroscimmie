package it.polimi.ingsw.client.view.cli.graphics.goals;

import it.polimi.ingsw.client.view.cli.graphics.grids.TableChars;
import it.polimi.ingsw.client.view.cli.graphics.simple.*;
import it.polimi.ingsw.client.view.cli.graphics.util.CliDrawer;
import it.polimi.ingsw.client.view.cli.graphics.util.ReplaceTarget;

/**
 * Represents the base of a goal card.
 */
public abstract class GoalCardBaseElement extends FramedElement {
    protected int width, height;

    /**
     * @param width width of the card.
     * @param height height of the card.
     */
    public GoalCardBaseElement(int width, int height) {
        super(width, height);
        this.width = width;
        this.height = height;
    }

    protected void addHeader(String title) {
        var horizontal = new RowElement(String.valueOf(TableChars.HORIZONTAL_BAR.getChar()).repeat(width));
        // horizontal break
        CliDrawer.superimposeElement(horizontal, this, 0, 2, ReplaceTarget.EMPTY);
        CliDrawer.superimposeElement(new RowElement(title), this, 2, 1, ReplaceTarget.EMPTY);
        // horizontal break left corner
        setCell(0, 2, new CliTextElement(TableChars.LEFT_T.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));
        // horizontal break right corner
        setCell(width - 1, 2, new CliTextElement(TableChars.RIGHT_T.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));
    }

    protected abstract void addPointsSpace();
    protected abstract void setCardImage(int id);
}
