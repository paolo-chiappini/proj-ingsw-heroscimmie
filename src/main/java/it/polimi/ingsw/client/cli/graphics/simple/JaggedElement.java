package it.polimi.ingsw.client.cli.graphics.simple;

import it.polimi.ingsw.client.cli.graphics.util.StringToCliTextConverter;

import java.util.LinkedList;

/**
 * Represents a simple jagged (non-regular) element.
 */
public class JaggedElement extends CliElement {
    /**
     * Creates a new jagged element based on the given block of text.
     * @param text block of text to embed in the element.
     */
    public JaggedElement(String text) {
        generateElement(text, CliForeColors.DEFAULT, CliBackColors.DEFAULT);
    }

    /**
     * Creates a new jagged element based on the given block of text and colors.
     * @param text block of text to embed in the element.
     * @param foregroundColor foreground color (color of the text).
     * @param backgroundColor background color.
     */
    public JaggedElement(String text, CliForeColors foregroundColor, CliBackColors backgroundColor) {
        generateElement(text, foregroundColor, backgroundColor);
    }

    private void generateElement(String text, CliForeColors foregroundColor, CliBackColors backgroundColor) {
        this.representation = new LinkedList<>();
        var rows = StringToCliTextConverter.convertBlock(text, foregroundColor, backgroundColor);
        this.representation.addAll(rows);
    }
}

