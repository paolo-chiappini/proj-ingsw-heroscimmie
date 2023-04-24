package it.polimi.ingsw.client.cli.graphics.grids;

import it.polimi.ingsw.client.cli.graphics.simple.RectangleElement;

/**
 * Represents a generic element that can be contained in a grid.
 * The element has size 3x2.
 */
public abstract class GridCellElement extends RectangleElement {
    public static final int WIDTH = 3;
    public static final int HEIGHT = 2;
    public GridCellElement() {
        super(WIDTH, HEIGHT);
    }
}
