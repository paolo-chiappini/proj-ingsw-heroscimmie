package it.polimi.ingsw.client.view.cli.graphics.grids;

import it.polimi.ingsw.client.view.cli.graphics.simple.RectangleElement;

/**
 * Represents a generic element that can be contained in a small grid.
 * The element has size 2x1.
 */
public abstract class SmallCellElement extends RectangleElement {
    public static final int WIDTH = 2;
    public static final int HEIGHT = 1;
    public SmallCellElement() {
        super(WIDTH, HEIGHT);
    }
}
