package it.polimi.ingsw.client.cli.graphics.simple;

import it.polimi.ingsw.client.cli.graphics.util.ICliRenderable;
import it.polimi.ingsw.client.cli.graphics.util.ICliRenderer;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a generic cli graphic element.
 */
public abstract class CliElement implements ICliRenderable {
    protected List<List<CliTextElement>> representation;

    public CliElement() {
        representation = new LinkedList<>();
    }

    /**
     * Sets a single cell of the object's representation to the
     * specified element.
     * @param x x coordinate of the cell to change.
     * @param y y coordinate of the cell to change.
     * @param textElement element to set at the specified coordinates.
     */
    public void setCell(int x, int y, CliTextElement textElement) {
        this.representation.get(y).set(x, textElement);
    }

    /**
     * Get a single element at the specified coordinates.
     * @param x x coordinate of the chosen cell.
     * @param y y coordinate of the chosen cell.
     * @return the element at the specified coordinates.
     */
    public CliTextElement getCell(int x, int y) {
        return this.representation.get(y).get(x);
    }

    @Override
    public String render(ICliRenderer renderer) {
        return renderer.render(this);
    }

    /**
     * @return the width of the element as the length of the longest
     * row in the element's representation.
     */
    public int getWidth() {
        int max = 0;
        for (List<CliTextElement> cliTextElements : representation) {
            max = Integer.max(max, cliTextElements.size());
        }
        return max;
    }

    /**
     * @return the height of the element.
     */
    public int getHeight() {
        return representation.size();
    }
}
