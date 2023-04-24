package it.polimi.ingsw.client.cli.graphics.util;

import it.polimi.ingsw.client.cli.graphics.simple.CliBackColors;
import it.polimi.ingsw.client.cli.graphics.simple.CliForeColors;
import it.polimi.ingsw.client.cli.graphics.simple.CliTextElement;

import java.util.LinkedList;
import java.util.List;

/**
 * Utility class for converting Strings into cli elements.
 */
public abstract class StringToCliTextConverter {
    private final static char EMPTY_SPACE = ' ';

    /**
     * Converts a String to a list of plain text elements.
     * @param string text to convert.
     * @return the converted string.
     */
    public static List<CliTextElement> convert(String string) {
        return convert(string, CliForeColors.DEFAULT, CliBackColors.DEFAULT);
    }

    /**
     * Converts a String to a list of colored text elements.
     * @param string text to convert.
     * @param foregroundColor foreground color (color of the text).
     * @param backgroundColor background color.
     * @return the converted string.
     */
    public static List<CliTextElement> convert(String string, CliForeColors foregroundColor, CliBackColors backgroundColor) {
        List<CliTextElement> convertedString = new LinkedList<>();
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == EMPTY_SPACE) convertedString.add(null);
            else {
                convertedString.add(new CliTextElement(string.charAt(i), foregroundColor, backgroundColor));
            }
        }
        return convertedString;
    }

    /**
     * Converts a block of text (multiple lines separated by \n) to a
     * list of plain elements.
     * @param string block to convert.
     * @return converted block.
     */
    public static List<List<CliTextElement>> convertBlock(String string) {
        return convertBlock(string, CliForeColors.DEFAULT, CliBackColors.DEFAULT);
    }

    /**
     * Converts a block of text (multiple lines separated by \n) to a
     * list of colored elements.
     * @param string block to convert.
     * @param foregroundColor foreground color (color of the text).
     * @param backgroundColor background color.
     * @return converted block.
     */
    public static List<List<CliTextElement>> convertBlock(String string, CliForeColors foregroundColor, CliBackColors backgroundColor) {
        List<List<CliTextElement>> convertedBlock = new LinkedList<>();
        String[] lines = string.split("\n");
        for (String line : lines) {
            convertedBlock.add(StringToCliTextConverter.convert(line, foregroundColor, backgroundColor));
        }
        return convertedBlock;
    }
}
