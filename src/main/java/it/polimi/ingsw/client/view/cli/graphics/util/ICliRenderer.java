package it.polimi.ingsw.client.view.cli.graphics.util;

import it.polimi.ingsw.client.view.cli.graphics.simple.CliElement;

/**
 * Represents an object capable of transforming a renderable object
 * into its text-based representation.
 */
public interface ICliRenderer {
    /**
     * @param cliElement element to draw.
     * @return the text representation of the element.
     */
    String render(CliElement cliElement);
}
