package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.interfaces.GameTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Tests on CommonGoalCard10")
public class CommonGoalCard9Test {

    CommonGoalCard9 card9;
    Bookshelf bookshelf;

    @Nested
    @DisplayName("On card creation")
    class CardCreationTests {
        @BeforeEach
        void createNewCard() {
            card9 = new CommonGoalCard9(2);
        }

        @Test
        @DisplayName("the card id should be 9")
        void cardId() {
            assertEquals(9, card9.getId());
        }

        @Nested
        @DisplayName("When checking if the card's goal is achieved")
        class CanPlaceTests {
            List<GameTile> tiles;
            List<GameTile> tiles1;

            @BeforeEach
            void createNewBookshelf() {
                bookshelf = new Bookshelf();
                tiles = new ArrayList<>();
                tiles1=new ArrayList<>();
            }

            @Test
            @DisplayName("should be true with 3 columns each formed by 6 tiles of maximum 3 different types.")
            void canObtainPoint3Columns3DifferentTiles() {
                tiles.add(new Tile(TileType.CAT));
                tiles.add(new Tile(TileType.BOOK));
                tiles.add(new Tile(TileType.TROPHY));
                tiles1.add(new Tile(TileType.PLANT));
                tiles1.add(new Tile(TileType.FRAME));
                tiles1.add(new Tile(TileType.BOOK));
                bookshelf.dropTiles(tiles,0);
                bookshelf.dropTiles(tiles,0);
                bookshelf.dropTiles(tiles1,1);
                bookshelf.dropTiles(tiles1,1);
                bookshelf.dropTiles(tiles,2);
                bookshelf.dropTiles(tiles,2);
                assertTrue(card9.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with 3 columns each formed by 6 tiles of maximum 2 different types.")
            void canObtainPoint3Columns2DifferentTiles()  {
                tiles.add(new Tile(TileType.CAT));
                tiles.add(new Tile(TileType.BOOK));
                tiles.add(new Tile(TileType.BOOK));
                tiles1.add(new Tile(TileType.PLANT));
                tiles1.add(new Tile(TileType.FRAME));
                tiles1.add(new Tile(TileType.PLANT));
                bookshelf.dropTiles(tiles,0);
                bookshelf.dropTiles(tiles,0);
                bookshelf.dropTiles(tiles1,1);
                bookshelf.dropTiles(tiles1,1);
                bookshelf.dropTiles(tiles,2);
                bookshelf.dropTiles(tiles,2);
                assertTrue(card9.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with 3 columns each formed by 6 tiles of the same type")
            void canObtainPoint3ColumnsSameTiles()  {
                tiles.add(new Tile(TileType.BOOK));
                tiles.add(new Tile(TileType.BOOK));
                tiles.add(new Tile(TileType.BOOK));
                tiles1.add(new Tile(TileType.PLANT));
                tiles1.add(new Tile(TileType.PLANT));
                tiles1.add(new Tile(TileType.PLANT));
                bookshelf.dropTiles(tiles,0);
                bookshelf.dropTiles(tiles,0);
                bookshelf.dropTiles(tiles1,1);
                bookshelf.dropTiles(tiles1,1);
                bookshelf.dropTiles(tiles,2);
                bookshelf.dropTiles(tiles,2);
                assertTrue(card9.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be true with 4 columns each formed by 6 tiles of maximum 3 different types.")
            void canObtainPoint4Columns3DifferentTiles()  {
                tiles.add(new Tile(TileType.CAT));
                tiles.add(new Tile(TileType.BOOK));
                tiles.add(new Tile(TileType.TROPHY));
                tiles1.add(new Tile(TileType.PLANT));
                tiles1.add(new Tile(TileType.FRAME));
                tiles1.add(new Tile(TileType.BOOK));
                bookshelf.dropTiles(tiles,0);
                bookshelf.dropTiles(tiles,0);
                bookshelf.dropTiles(tiles1,1);
                bookshelf.dropTiles(tiles1,1);
                bookshelf.dropTiles(tiles,2);
                bookshelf.dropTiles(tiles,2);
                bookshelf.dropTiles(tiles1,3);
                bookshelf.dropTiles(tiles1,3);
                assertTrue(card9.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with 3 columns each formed by 6 tiles of 4 different types.")
            void canNotObtainPoint3Columns4DifferentTiles()  {
                tiles.add(new Tile(TileType.BOOK));
                tiles.add(new Tile(TileType.TROPHY));
                tiles.add(new Tile(TileType.TOY));
                tiles1.add(new Tile(TileType.CAT));
                tiles1.add(new Tile(TileType.PLANT));
                tiles1.add(new Tile(TileType.PLANT));
                bookshelf.dropTiles(tiles,0);
                bookshelf.dropTiles(tiles1,0);
                bookshelf.dropTiles(tiles,1);
                bookshelf.dropTiles(tiles1,1);
                bookshelf.dropTiles(tiles,2);
                bookshelf.dropTiles(tiles1,2);
                assertFalse(card9.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with columns not formed by 6 tiles.")
            void canNotObtainPointNot6Tiles()  {
                tiles.add(new Tile(TileType.BOOK));
                tiles.add(new Tile(TileType.TROPHY));
                tiles1.add(new Tile(TileType.CAT));
                tiles1.add(new Tile(TileType.PLANT));
                bookshelf.dropTiles(tiles,0);
                bookshelf.dropTiles(tiles1,0);
                bookshelf.dropTiles(tiles,1);
                bookshelf.dropTiles(tiles1,1);
                bookshelf.dropTiles(tiles,2);
                bookshelf.dropTiles(tiles1,2);
                assertFalse(card9.canObtainPoints(bookshelf));
            }

            @Test
            @DisplayName("should be false with 2 columns each formed by 6 tiles of maximum 3 different types.")
            void canNotObtainPoint2Columns(){
                tiles.add(new Tile(TileType.BOOK));
                tiles.add(new Tile(TileType.TROPHY));
                tiles.add(new Tile(TileType.BOOK));
                tiles1.add(new Tile(TileType.TROPHY));
                tiles1.add(new Tile(TileType.PLANT));
                tiles1.add(new Tile(TileType.PLANT));
                bookshelf.dropTiles(tiles,0);
                bookshelf.dropTiles(tiles1,0);
                bookshelf.dropTiles(tiles,1);
                bookshelf.dropTiles(tiles1,1);
                assertFalse(card9.canObtainPoints(bookshelf));
            }
        }
    }
}