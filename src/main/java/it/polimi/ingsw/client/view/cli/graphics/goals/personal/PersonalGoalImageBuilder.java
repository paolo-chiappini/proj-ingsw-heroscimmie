package it.polimi.ingsw.client.view.cli.graphics.goals.personal;

import it.polimi.ingsw.client.view.cli.graphics.bookshelf.SmallBookshelfElement;
import it.polimi.ingsw.client.view.cli.graphics.tiles.SmallTileElement;
import it.polimi.ingsw.server.model.tile.TileType;
import it.polimi.ingsw.util.FileIOManager;
import it.polimi.ingsw.util.FilePath;
import org.json.JSONArray;

import java.io.FileNotFoundException;

/**
 * Represents a factory class for personal goal card "images".
 */
public abstract class PersonalGoalImageBuilder {
    private static final String TEMPLATE_FILE = "personal_goals_cli_templates.json";
    private static final TileType[][][] templates = loadCardsTemplates();

    private static TileType[][][] loadCardsTemplates() {
        TileType[][][] templates;
        String json;
        try {
            json = FileIOManager.readFromFile(TEMPLATE_FILE, FilePath.TEMPLATES);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        JSONArray array = new JSONArray(json);
        templates = new TileType[array.length()][6][5];
        for (int i = 0; i < array.length(); i++) {
            JSONArray currTemplate = array.getJSONArray(i);
            for (int j = 0; j < currTemplate.length(); j++) {
                JSONArray currRow = currTemplate.getJSONArray(j);
                for (int k = 0; k < currRow.length(); k++) {
                    if (currRow.getInt(k) < 0) templates[i][j][k] = null;
                    else templates[i][j][k] = TileType.values()[currRow.getInt(k)];
                }
            }
        }
        return templates;
    }

    /**
     * @param id card id.
     * @return the cli representation of the requested card.
     */
    public static SmallBookshelfElement getPersonalGoalById(int id) {
        SmallBookshelfElement smallBookshelfElement = new SmallBookshelfElement();
        TileType[][] requested = templates[id];
        for (int i = 0; i < requested.length; i++) {
            for (int j = 0; j < requested[i].length; j++) {
                if (requested[i][j] == null) continue;
                smallBookshelfElement.setElement(new SmallTileElement(requested[i][j]), j, i);
            }
        }
        return smallBookshelfElement;
    }
}
