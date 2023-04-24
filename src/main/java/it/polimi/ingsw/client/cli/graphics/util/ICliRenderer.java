package it.polimi.ingsw.client.cli.graphics.util;

import it.polimi.ingsw.client.cli.graphics.simple.CliElement;

/**
 * Represents an object capable of transforming a renderable object
 * into its text-based representation.
 */
public interface ICliRenderer {
    /**
     * @param cliElement element to render.
     * @return the text representation of the element.
     */
    String render(ICliRenderable cliElement);
}
