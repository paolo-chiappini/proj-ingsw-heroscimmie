package it.polimi.ingsw.client.view.cli.graphics.info;

import it.polimi.ingsw.client.view.cli.graphics.bookshelf.BookshelfElement;
import it.polimi.ingsw.client.view.cli.graphics.grids.TableChars;
import it.polimi.ingsw.client.view.cli.graphics.simple.*;
import it.polimi.ingsw.client.view.cli.graphics.util.CliDrawer;
import it.polimi.ingsw.client.view.cli.graphics.util.ReplaceTarget;

/**
 * Represents an element displaying information regarding
 * bonus points that can be obtained through bonuses.
 */
public class BonusesInfoElement extends FramedElement {
    private static final int HEIGHT = 6;
    private static final int WIDTH = new BookshelfElement().getWidth();
    private static final String template =
            """
            3 [==]: 2pt     5 [==]: 5pt
            4 [==]: 3pt     6+[==]: 8pt
             
            First to end: 1pt
            """;

    public BonusesInfoElement() {
        super(WIDTH, HEIGHT);

        var templateToElement = new JaggedElement(template);
        CliDrawer.superimposeElement(templateToElement, this, 2,1, ReplaceTarget.EMPTY);

        // add horizontal break
        CliDrawer.superimposeElement(
                new RowElement(String.valueOf(TableChars.HORIZONTAL_BAR.getChar()).repeat(WIDTH)),
                this, 0, HEIGHT - 3, ReplaceTarget.EMPTY
        );
        this.setCell(0, HEIGHT - 3, new CliTextElement(TableChars.LEFT_T.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));
        this.setCell(WIDTH - 1, HEIGHT - 3, new CliTextElement(TableChars.RIGHT_T.getChar(), CliForeColors.DEFAULT, CliBackColors.DEFAULT));
    }
}
