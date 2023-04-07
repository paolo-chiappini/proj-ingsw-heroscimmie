package it.polimi.ingsw.util;

/**
 * Represents an object that can be serialized and deserialized
 * to and from a string of data.
 */
public interface Serializable {
    /**
     * Serializes the object to a String of data.
     * @param serializer type of serializer used to serialize data.
     * @return a String of data representing the serialized class.
     */
    String serialize(Serializer serializer);
}
