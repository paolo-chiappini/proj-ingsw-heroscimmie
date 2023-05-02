package it.polimi.ingsw.client.cli.graphics.goals.common;

import it.polimi.ingsw.client.cli.graphics.simple.JaggedElement;
import it.polimi.ingsw.util.FileIOManager;
import it.polimi.ingsw.util.FilePath;
import org.json.JSONArray;

import java.io.FileNotFoundException;

/**
 * Represents a factory class for common goal card "images".
 */
public abstract class CommonGoalImageBuilder {
    private static final String TEMPLATE_FILE = "common_goals_cli_templates.json";
    private static final String[] stringRepresentations = loadCardsTemplates();

    private static String[] loadCardsTemplates() {
        String[] templates;
        String json;
        try {
            json = FileIOManager.readFromFile(TEMPLATE_FILE, FilePath.TEMPLATES);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        JSONArray array = new JSONArray(json);
        templates = new String[array.length()];
        for (int i = 0; i < array.length(); i++) {
            templates[i] = array.getString(i);
        }
        return templates;
    }

    /**
     * @param id card id.
     * @return the cli representation of the requested card.
     */
    public static JaggedElement getCommonGoalById(int id) {
        String requested = stringRepresentations[id];
        return new JaggedElement(requested);
    }
}
