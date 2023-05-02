package it.polimi.ingsw.client.cli.graphics.util;

import it.polimi.ingsw.client.cli.graphics.simple.CliElement;

/**
 * Utility class for composing graphic elements.
 */
public abstract class CliDrawer {
    /**
     * Composes two elements by adding one on top of the other.
     * @param element element to superimpose.
     * @param base base element to add the first to.
     * @param x x coordinate to start superimposing element.
     * @param y y coordinate to start superimposing element.
     * @param replaceTarget replacement method.
     */
    public static void superimposeElement(CliElement element, CliElement base, int x, int y, ReplaceTarget replaceTarget) {
        for (int dy = 0; dy < element.getHeight(); dy++) {
            if (y + dy >= base.getHeight()) break;
            for (int dx = 0; dx < element.getRowWidth(dy); dx++) {
                if (x + dx >= base.getRowWidth(dy)) break;

                if (replaceTarget == ReplaceTarget.EMPTY && base.getCell(x+dx, y+dy) != null) continue;
                base.setCell(x+dx ,y+dy, element.getCell(dx, dy));
            }
        }
    }

    /**
     * Composes two elements by adding one on top of the other (default replacement is "ALL").
     * @param element element to superimpose.
     * @param base base element to add the first to.
     * @param x x coordinate to start superimposing element.
     * @param y y coordinate to start superimposing element.
     */
    public static void superimposeElement(CliElement element, CliElement base, int x, int y) {
        superimposeElement(element, base, x, y, ReplaceTarget.ALL);
    }

    /**
     * Clears the specified area by replacing it with empty cells.
     * @param element element to clear.
     * @param startX x coordinate of the top left corner of the area to clear.
     * @param startY y coordinate of the top left corner of the area to clear.
     * @param endX x coordinate of the bottom right corner of the area to clear.
     * @param endY y coordinate of the bottom right corner of the area to clear.
     */
    public static void clearArea(CliElement element, int startX, int startY, int endX, int endY) {
        for (int dy = startY; dy <= endY; dy++) {
            if (dy >= element.getHeight()) break;
            for (int dx = startX; dx <= endX; dx++) {
                if (dx >= element.getRowWidth(dy)) break;
                element.setCell(dx, dy, null);
            }
        }
    }
}
