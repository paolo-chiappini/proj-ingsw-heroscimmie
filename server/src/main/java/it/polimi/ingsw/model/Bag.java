package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.IllegalActionException;
import it.polimi.ingsw.model.interfaces.GameTile;
import it.polimi.ingsw.model.interfaces.IBag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Bag implements IBag {
    private final HashMap<TileType,Integer> tilesBag;
    private static final int NUM_OF_TILES=22;

    public Bag() {
        this.tilesBag = new HashMap<>();
        for (TileType tile:TileType.values()) {
            tilesBag.put(tile,NUM_OF_TILES);
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
            throw new IllegalActionException("The bag is empty");
        ArrayList<TileType> tileTypes = new ArrayList<>(tilesBag.keySet());
        Collections.shuffle(tileTypes);
        int initialNumber=tilesBag.get(tileTypes.get(0));
        tilesBag.replace(tileTypes.get(0),initialNumber-1);
        if(tilesBag.get(tileTypes.get(0))==0)
            tilesBag.remove(tileTypes.get(0));
        return new Tile(tileTypes.get(0));
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
            tilesBag.computeIfPresent(tile.getType(),(key, val) -> val + 1);
    }

    /**
     * Draws a tile of a given type from the bag
     * @return a tile
     */
    @Override
    public GameTile getTileByType(TileType type) {
        if(tilesBag.isEmpty())
            throw new IllegalActionException("The bag is empty");
        int initialNumber=tilesBag.get(type);
        tilesBag.replace(type,initialNumber-1);
        if(tilesBag.get(type)==0)
            tilesBag.remove(type);
        return new Tile(type);
    }

    /**
     * Sets the number of remaining tiles of a type
     * @param type is the tile type to set
     * @param count is the number of remaining tiles
     */
    @Override
    public void setRemainingTilesCount(TileType type, int count) {
        tilesBag.replace(type,count);
    }
}