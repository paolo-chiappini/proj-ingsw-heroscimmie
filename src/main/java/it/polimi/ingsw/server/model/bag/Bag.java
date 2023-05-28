package it.polimi.ingsw.server.model.bag;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.server.model.tile.GameTile;
import it.polimi.ingsw.server.model.tile.Tile;
import it.polimi.ingsw.server.model.tile.TileType;

import java.util.*;

public class Bag implements IBag {
    private final HashMap<TileType, Integer> tilesBag;
    private final HashMap<TileType, GameTile> tileInstances;
    private static final int NUM_OF_TILES = 22;

    public Bag() {
        this.tilesBag = new HashMap<>();
        this.tileInstances = new HashMap<>();

        for (TileType tile : TileType.values()) {
            tilesBag.put(tile, NUM_OF_TILES);
            tileInstances.put(tile, new Tile(tile));
        }
    }

    @Override
    public HashMap<TileType, Integer> getTilesBag() {
        return new HashMap<>(tilesBag);
    }

    /**
     * Draws a tile from the bag
     * @return a tile
     */
    @Override
    public GameTile drawTile()
    {
        if(tilesBag.isEmpty())
        {
            throw new IllegalActionException("The bag is empty");
        }
        LinkedList<TileType> tileTypes = new LinkedList<>(tilesBag.keySet());
        Collections.shuffle(tileTypes);
        int initialNumber=tilesBag.get(tileTypes.getFirst());
        tilesBag.replace(tileTypes.getFirst(),initialNumber-1);
        if(tilesBag.get(tileTypes.getFirst()) == 0)
        {
            tilesBag.remove(tileTypes.getFirst());
        }
        return tileInstances.get(tileTypes.getFirst());
    }

    /**
     * Adds a tile to the bag
     */
    @Override
    public void addTile(GameTile tile)
    {
        if(tilesBag.values().stream().reduce(0,Integer::sum)>=132)
            throw new IllegalActionException("The bag is full");
        else
            tilesBag.computeIfPresent(tile.type(),(key, val) -> val + 1);
    }

    /**
     * Draws a tile of a given type from the bag
     * @return a tile
     */
    @Override
    public GameTile getTileByType(TileType type) {
        if(tilesBag.isEmpty())
            throw new IllegalActionException("The bag is empty");
        if(tilesBag.get(type)==null)
            return null;
        Integer initialNumber=tilesBag.get(type);
        tilesBag.replace(type,initialNumber-1);
        if(tilesBag.get(type)==0)
            tilesBag.remove(type);
        return new Tile(type);
    }
}