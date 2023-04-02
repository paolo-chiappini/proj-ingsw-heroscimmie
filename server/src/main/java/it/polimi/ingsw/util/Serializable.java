package it.polimi.ingsw.util;

public interface Serializable {
    String serialize(Serializer serializer);
    void deserialize(Deserializer deserializer, String data);
}
