package it.polimi.ingsw.util;

/**
 * Predefined paths for managing local resources.
 */
public enum FilePath {
    RESOURCES_DATA("/data"),
    RESOURCES_TEMPLATES(RESOURCES_DATA.path + "/templates"),
    SAVED("./saved"),
    TEST("/test_files");

    private final String path;
    FilePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
