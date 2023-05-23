package it.polimi.ingsw.util;

/**
 * Predefined paths for managing local resources.
 */
public enum FilePath {
    DEFAULT("/data"),
    TEMPLATES(DEFAULT.getPath() + "/templates"),
    SAVED(DEFAULT.getPath() + "/saved"),
    TEST("/test_files");

    private final String path;
    FilePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
