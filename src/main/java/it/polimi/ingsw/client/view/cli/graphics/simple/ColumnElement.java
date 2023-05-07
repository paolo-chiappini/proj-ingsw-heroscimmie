package it.polimi.ingsw.client.view.cli.graphics.simple;

import it.polimi.ingsw.client.view.cli.graphics.util.StringToCliTextConverter;

import java.util.LinkedList;

/**
 * Represents a simple column element.
 */
public class ColumnElement extends CliElement {
    /**
     * Creates a new column element based on the given string.
     * @param text String to embed in the element.
     */
    public ColumnElement(String text) {
        generateElement(text, CliForeColors.DEFAULT, CliBackColors.DEFAULT);
    }

    /**
     * Creates a new column element based on the given string and colors.
     * @param text String to embed in the element.
     * @param foregroundColor foreground color (color of the text).
     * @param backgroundColor background color.
     */
    public ColumnElement(String text, CliForeColors foregroundColor, CliBackColors backgroundColor) {
        generateElement(text, foregroundColor, backgroundColor);
    }

    private void generateElement(String text, CliForeColors foregroundColor, CliBackColors backgroundColor) {
        this.representation = new LinkedList<>();
        var column = StringToCliTextConverter.convert(text, foregroundColor, backgroundColor);
        for (var row : column) {
            var tempList = new LinkedList<CliTextElement>();
            tempList.add(row);
            this.representation.add(tempList);
        }
    }
}
