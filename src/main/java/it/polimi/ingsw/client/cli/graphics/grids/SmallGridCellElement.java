package it.polimi.ingsw.client.cli.graphics.grids;

import it.polimi.ingsw.client.cli.graphics.simple.CliElement;
import it.polimi.ingsw.client.cli.graphics.simple.RectangleElement;

import java.util.LinkedList;

/**
 * Represents a generic element that can be contained in a small grid.
 * The element has size 2x1.
 */
public abstract class SmallGridCellElement extends RectangleElement {
    private static final int WIDTH = 2;
    private static final int HEIGHT = 1;
    public SmallGridCellElement() {
        super(WIDTH, HEIGHT);
    }
}
