package it.polimi.ingsw.client.cli.graphics.grids;

import it.polimi.ingsw.client.cli.graphics.simple.CliElement;
import it.polimi.ingsw.client.cli.graphics.simple.RectangleElement;

import java.util.LinkedList;

/**
 * Represents a generic element that can be contained in a grid.
 * The element has size 3x2.
 */
public abstract class GridCellElement extends RectangleElement {
    private static final int WIDTH = 3;
    private static final int HEIGHT = 2;
    public GridCellElement() {
        super(WIDTH, HEIGHT);
    }
}
