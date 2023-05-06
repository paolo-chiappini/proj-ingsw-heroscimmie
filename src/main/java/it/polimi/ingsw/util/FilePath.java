package it.polimi.ingsw.util;

/**
 * Predefined paths for managing local resources.
 */
public enum FilePath {
    DEFAULT("./resources/data"),
    TEMPLATES(DEFAULT.getPath() + "/templates"),
    SAVED(DEFAULT.getPath() + "/saved");

    private final String path;
    FilePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
