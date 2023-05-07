package it.polimi.ingsw.client.view.cli.graphics.simple;

/**
 * Ansi codes for background text colors.
 */
public enum CliBackColors {
    DEFAULT(""),
    BLACK("\u001B[40m"),
    RED("\u001B[41m"),
    GREEN("\u001B[42m"),
    YELLOW("\u001B[43m"),
    BLUE("\u001B[44m"),
    PURPLE("\u001B[45m"),
    CYAN("\u001B[46m"),
    WHITE("\u001B[47m"),
    BRIGHT_BLACK("\u001B[100m"),
    BRIGHT_RED("\u001B[101m"),
    BRIGHT_GREEN("\u001B[102m"),
    BRIGHT_YELLOW("\u001B[103m"),
    BRIGHT_BLUE("\u001B[104m"),
    BRIGHT_PURPLE("\u001B[105m"),
    BRIGHT_CYAN("\u001B[106m"),
    BRIGHT_WHITE("\u001B[107m");

    private final String ansiCode;
    CliBackColors(String ansiCode) {
        this.ansiCode = ansiCode;
    }

    public String getAnsiCode() { return ansiCode; }
}