package it.polimi.ingsw.client.cli.graphics.util;

/**
 * Represents an object that can be shown in a text-based representation.
 */
public interface ICliRenderable {
    /**
     * @param renderer type of renderer used to transform the
     *                 object into text.
     * @return the string representation of the object.
     */
    String render(ICliRenderer renderer);
}
