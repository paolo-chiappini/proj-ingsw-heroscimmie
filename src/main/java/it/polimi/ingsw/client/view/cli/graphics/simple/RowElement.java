package it.polimi.ingsw.client.view.cli.graphics.simple;

import it.polimi.ingsw.client.view.cli.graphics.util.StringToCliTextConverter;

import java.util.LinkedList;

/**
 * Represents a simple row element.
 */
public class RowElement extends CliElement {
    /**
     * Creates a new row element based on the given string.
     * @param text String to embed in the element.
     */
    public RowElement(String text) {
        generateElement(text, CliForeColors.DEFAULT, CliBackColors.DEFAULT);
    }

    /**
     * Creates a new row element based on the given string and colors.
     * @param text String to embed in the element.
     * @param foregroundColor foreground color (color of the text).
     * @param backgroundColor background color.
     */
    public RowElement(String text, CliForeColors foregroundColor, CliBackColors backgroundColor) {
        generateElement(text, foregroundColor, backgroundColor);
    }

    private void generateElement(String text, CliForeColors foregroundColor, CliBackColors backgroundColor) {
        this.representation = new LinkedList<>();
        this.representation.add(StringToCliTextConverter.convert(text, foregroundColor, backgroundColor));
    }
}
