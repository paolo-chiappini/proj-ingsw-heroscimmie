package it.polimi.ingsw.client.cli.graphics.goals.common;

import it.polimi.ingsw.client.cli.graphics.goals.GoalCardBaseElement;
import it.polimi.ingsw.client.cli.graphics.grids.TableChars;
import it.polimi.ingsw.client.cli.graphics.simple.CliElement;
import it.polimi.ingsw.client.cli.graphics.simple.RowElement;
import it.polimi.ingsw.client.cli.graphics.util.CliDrawer;
import it.polimi.ingsw.client.cli.graphics.util.ReplaceTarget;

/**
 * Represents a common goal card.
 */
public class CommonGoalCardElement extends GoalCardBaseElement {
    public static final int HEIGHT = 10;
    public static final int WIDTH = 22;

    /**
     * @param title title of the card.
     * @param id id of the card.
     * @param points points remaining.
     */
    public CommonGoalCardElement(String title, int id, int points) {
        super(WIDTH, HEIGHT);
        addHeader(title);
        addPointsSpace();
        setPoints(points);
        setCardImage(id);
    }

    /**
     * Set the points displayed on the card.
     * @param points points remaining.
     */
    public void setPoints(int points) {
        // clear space
        CliDrawer.clearArea(this, WIDTH - 3, HEIGHT - 2, WIDTH - 2, HEIGHT - 2);
        // set points
        CliDrawer.superimposeElement(new RowElement(String.valueOf(points)),this,WIDTH - 3, HEIGHT - 2, ReplaceTarget.EMPTY);
    }

    @Override
    protected void addPointsSpace() {
        String pointsSpace = " pt" + "\n" +
            TableChars.TOP_LEFT_CORNER.getChar() +
            TableChars.HORIZONTAL_BAR.getChar() + TableChars.HORIZONTAL_BAR.getChar() +
            TableChars.RIGHT_T.getChar() + "\n" +
            TableChars.VERTICAL_BAR.getChar() +
            TableChars.SPACE.getChar() + TableChars.SPACE.getChar() + "\n" +
            TableChars.BOTTOM_T.getChar();

        var lines = pointsSpace.split("\n");
        for (int i = 0; i < lines.length; i++) {
            CliDrawer.superimposeElement(new RowElement(lines[i]),this, WIDTH - 4 , HEIGHT - lines.length + i, ReplaceTarget.ALL);
        }
    }

    @Override
    protected void setCardImage(int id) {
        CliElement img = CommonGoalImageBuilder.getCommonGoalById(id);
        CliDrawer.superimposeElement(img, this, 1, 3, ReplaceTarget.EMPTY);
    }
}
