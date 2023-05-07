package it.polimi.ingsw.client.view.cli.graphics.grids;

import it.polimi.ingsw.client.view.cli.graphics.simple.RectangleElement;

/**
 * Represents a generic element that can be contained in a grid.
 * The element has size 3x2.
 */
public abstract class CellElement extends RectangleElement {
    public static final int WIDTH = 3;
    public static final int HEIGHT = 2;
    public CellElement() {
        super(WIDTH, HEIGHT);
    }
}
