package it.polimi.ingsw.client.view.cli.graphics.goals.personal;

import it.polimi.ingsw.client.view.cli.graphics.goals.GoalCardBaseElement;
import it.polimi.ingsw.client.view.cli.graphics.grids.TableChars;
import it.polimi.ingsw.client.view.cli.graphics.simple.*;
import it.polimi.ingsw.client.view.cli.graphics.util.CliDrawer;
import it.polimi.ingsw.client.view.cli.graphics.util.ReplaceTarget;

/**
 * Represents a personal goal card.
 */
public class PersonalGoalCardElement extends GoalCardBaseElement {
    private static final int WIDTH = 22;
    private static final int HEIGHT = 13;

    public PersonalGoalCardElement(String title, int id) {
        super(WIDTH, HEIGHT);

        addHeader(title);
        addPointsSpace();
        setCardImage(id);
    }

    @Override
    protected void addPointsSpace() {
        var horizontal = new RowElement(String.valueOf(TableChars.HORIZONTAL_BAR.getChar()).repeat(width));
        var vertical = new ColumnElement(String.valueOf(TableChars.VERTICAL_BAR.getChar()).repeat(4));
        final int[] points = new int[] {2, 3, 4, 6, 9, 12};
        StringBuilder matchesTemplate = new StringBuilder();
        StringBuilder pointsTemplate = new StringBuilder();

        CliDrawer.superimposeElement(horizontal, this, 0, HEIGHT - 4, ReplaceTarget.ALL);
        for (int i = 0; i < points.length; i++) {
            int offset = 3 + i * 3;
            CliDrawer.superimposeElement(vertical, this, offset, HEIGHT - 3, ReplaceTarget.EMPTY);
            // set corners
            this.setCell(offset, HEIGHT - 4, new CliTextElement(TableChars.TOP_T.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));
            this.setCell(offset, HEIGHT - 1, new CliTextElement(TableChars.BOTTOM_T.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));
            // create points template
            matchesTemplate.append(i + 1).append(TableChars.SPACE.getChar()).append(TableChars.SPACE.getChar());
            pointsTemplate.append(points[i]).append(TableChars.SPACE.getChar()).append(TableChars.SPACE.getChar());
        }
        matchesTemplate.insert(0, "ok ");
        pointsTemplate.insert(0, "pt ");

        // set corners
        this.setCell(0, HEIGHT - 4, new CliTextElement(TableChars.LEFT_T.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));
        this.setCell(WIDTH - 1, HEIGHT - 4, new CliTextElement(TableChars.RIGHT_T.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));
        // set points
        CliDrawer.superimposeElement(new RowElement(matchesTemplate.toString()),this, 1, HEIGHT - 3, ReplaceTarget.EMPTY);
        CliDrawer.superimposeElement(new RowElement(pointsTemplate.toString()),this, 1, HEIGHT - 2, ReplaceTarget.EMPTY);
    }

    @Override
    protected void setCardImage(int id) {
        CliElement img = PersonalGoalImageBuilder.getPersonalGoalById(id);
        CliDrawer.superimposeElement(img, this, 3, 3, ReplaceTarget.EMPTY);
    }
}
