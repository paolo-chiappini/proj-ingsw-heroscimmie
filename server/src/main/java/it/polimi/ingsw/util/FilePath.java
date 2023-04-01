package it.polimi.ingsw.util;

public enum FilePath {
    DEFAULT("./server/resources/data"),
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
