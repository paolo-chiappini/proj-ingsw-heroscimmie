package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Bag {
    private HashMap<TileType,Integer> tilesBag;
    private static final int NUM_OF_TILES=22;

    public Bag() {
        this.tilesBag = new HashMap<>();
        for (TileType tile:TileType.values()) {
            tilesBag.put(tile,NUM_OF_TILES);
        }
    }

    public HashMap<TileType, Integer> getTilesBag() {
        return tilesBag;
    }

    /**
     * Draw a tile from the bag
     * @return a tile
     **/
    public Tile drawTile()
    {
        ArrayList<TileType> tileTypes = new ArrayList<>(tilesBag.keySet());
        Collections.shuffle(tileTypes);
        int initialNumber=tilesBag.get(tileTypes.get(0));
        tilesBag.replace(tileTypes.get(0),initialNumber-1);
        if(tilesBag.get(tileTypes.get(0))==0)
        {
            tilesBag.remove(tileTypes.get(0));
        }
        return new Tile(tileTypes.get(0));
    }
}