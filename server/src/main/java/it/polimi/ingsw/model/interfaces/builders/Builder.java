package it.polimi.ingsw.model.interfaces.builders;

/**
 * Represents a generic builder.
 * @param <T> type of object created by the builder.
 */
public interface Builder<T> {
    /**
     * Creates a new instance of the desired object.
     * @return the instance of the object built.
     */
    T build();
}
