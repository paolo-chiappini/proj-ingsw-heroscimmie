package it.polimi.ingsw.client.cli.graphics.simple;

/**
 * Represents a single character.
 * @param text character to represent.
 * @param foregroundColor color of the text.
 * @param backgroundColor color of the background.
 */
public record CliTextElement(char text, CliForeColors foregroundColor, CliBackColors backgroundColor) {
    /**
     * @return the colored text as a string.
     */
    public String getColoredText() {
        return foregroundColor.getAnsiCode() + backgroundColor().getAnsiCode() + text + CliForeColors.DEFAULT;
    }

    /**
     * @return the uncolored text as a string.
     */
    public String getPlainText() {
        return String.valueOf(text);
    }
}
