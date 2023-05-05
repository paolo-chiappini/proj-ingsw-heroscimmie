package it.polimi.ingsw.client.view.cli.graphics.util;

import it.polimi.ingsw.client.view.cli.graphics.simple.CliElement;
import it.polimi.ingsw.client.view.cli.graphics.simple.CliTextElement;

public class SimpleTextRenderer implements ICliRenderer {
    private static final String NEW_LINE = "\n";
    private static final String SPACE = " ";

    @Override
    public String render(CliElement cliElement) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < cliElement.getHeight(); i++) {
            for (int j = 0; j < cliElement.getRowWidth(i); j++) {
                CliTextElement cell = cliElement.getCell(j, i);
                if (cell == null) builder.append(SPACE);
                else builder.append(cell.getPlainText());
            }
            builder.append(NEW_LINE);
        }

        return builder.toString();
    }
}
