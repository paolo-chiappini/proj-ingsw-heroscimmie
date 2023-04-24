package it.polimi.ingsw.client.cli.graphics.bookshelf;

import it.polimi.ingsw.client.cli.graphics.grids.SmallGridCellElement;
import it.polimi.ingsw.client.cli.graphics.grids.SmallTileGridElement;

/**
 * Represents a smaller version of the player's bookshelf.
 */
public class SmallBookshelfElement extends SmallTileGridElement {
    private static final int WIDTH = 5;
    private static final int HEIGHT = 6;
    public SmallBookshelfElement() {
        this.contents = new SmallGridCellElement[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            this.representation.add(SmallTileGridElement.generateRow(WIDTH));
        }
    }
}
