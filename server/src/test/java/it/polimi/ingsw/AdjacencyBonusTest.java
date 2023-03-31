package it.polimi.ingsw;

import it.polimi.ingsw.model.AdjacencyBonusGoal;
import it.polimi.ingsw.model.TileType;
import it.polimi.ingsw.model.interfaces.GameTile;
import it.polimi.ingsw.model.interfaces.IBookshelf;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test on AdjacencyBonus")
public class AdjacencyBonusTest {
    private static AdjacencyBonusGoal adjacencyBonusGoal;
    private static class TestBookshelf implements IBookshelf {
        private final GameTile[][] tiles;

        public TestBookshelf(int[][] template) {
            tiles = new GameTile[template.length][template[0].length];
            for (int i = 0; i < template.length; i++) {
                for (int j = 0; j < template[i].length; j++) {
                    if(template[i][j] < 0) {
                        tiles[i][j] = null;
                        continue;
                    }

                    TileType type = intToTileType(template[i][j]);
                    tiles[i][j] = () -> type;
                }
            }
        }

        private TileType intToTileType(int n) {
            return TileType.values()[n];
        }
        @Override
        public GameTile getTileAt(int row, int column) {
            return tiles[row][column];
        }
        @Override
        public int getWidth() {
            return tiles[0].length;
        }
        @Override
        public int getHeight() {
            return tiles.length;
        }
        @Override
        public boolean isFull() {
            return false;
        }
        @Override
        public void dropTiles(List<GameTile> tilesToDrop, int column) {}
        @Override
        public boolean canDropTiles(int numOfTiles, int column) {
            return false;
        }
        @Override
        public boolean hasTile(int row, int column) {
            return false;
        }
        @Override
        public boolean compareTiles(int row, int column, int row2, int column2) {
            return false;
        }
    }

    @BeforeAll
    public static void init() {
        adjacencyBonusGoal = new AdjacencyBonusGoal();
    }

    @Nested
    @DisplayName("When evaluating points")
    class EvaluatePointsTests {
        @Test
        @DisplayName("on empty grid, should return 0")
        void evaluateOnEmptyGrid() {
            int[][] template = new int[][] {
                    { -1, -1, -1},
                    { -1, -1, -1}
            };
            IBookshelf grid = new TestBookshelf(template);

            assertEquals(adjacencyBonusGoal.evaluatePoints(grid), 0);
        }

        @Test
        @DisplayName("on non empty grid with small groups (size < 3)")
        void evaluateOnNonEmptyGridSmallGroups() {
            int[][] template = new int[][] {
                    {1, 2, 2},
                    {2, 3, 1}
            };
            IBookshelf grid = new TestBookshelf(template);

            assertEquals(0, adjacencyBonusGoal.evaluatePoints(grid));
        }

        @Test
        @DisplayName("on non empty grid with one size 3 group")
        void evaluateOnNonEmptyGridOneSize3Group() {
            int[][] template = new int[][] {
                    {1, 2, 2},
                    {1, 2, 1}
            };
            IBookshelf grid = new TestBookshelf(template);

            assertEquals(2, adjacencyBonusGoal.evaluatePoints(grid));
        }

        @Test
        @DisplayName("on non empty grid with one size 4 group")
        void evaluateOnNonEmptyGridOneSize4Group() {
            int[][] template = new int[][] {
                    {1, 2, 2},
                    {2, 2, 1}
            };
            IBookshelf grid = new TestBookshelf(template);

            assertEquals(3, adjacencyBonusGoal.evaluatePoints(grid));
        }

        @Test
        @DisplayName("on non empty grid with one size 5 group")
        void evaluateOnNonEmptyGridOneSize5Group() {
            int[][] template = new int[][] {
                    {1, 2, 2},
                    {2, 2, 2}
            };
            IBookshelf grid = new TestBookshelf(template);

            assertEquals(5, adjacencyBonusGoal.evaluatePoints(grid));
        }

        @Test
        @DisplayName("on non empty grid with one size 6 group")
        void evaluateOnNonEmptyGridOneSize6Group() {
            int[][] template = new int[][] {
                    {2, 2, 2},
                    {2, 2, 2}
            };
            IBookshelf grid = new TestBookshelf(template);

            assertEquals(8, adjacencyBonusGoal.evaluatePoints(grid));
        }

        @Test
        @DisplayName("on non empty grid with one size 6+ group")
        void evaluateOnNonEmptyGridOneSize6PlusGroup() {
            int[][] template = new int[][] {
                    {1, 2, 2},
                    {2, 2, 2},
                    {2, 0, 2}
            };
            IBookshelf grid = new TestBookshelf(template);

            assertEquals(8, adjacencyBonusGoal.evaluatePoints(grid));
        }
        @Test
        @DisplayName("on non empty grid with mixed groups")
        void evaluateOnNonEmptyGridMixedGroups() {
            // example on rulebook
            int[][] template = new int[][] {
                    { 1,  1,  1, -1, -1},
                    { 2,  1,  1,  3, -1},
                    { 0,  2,  0,  2, -1},
                    { 4,  5,  4,  5, -1},
                    { 4,  4,  3,  3,  3},
                    { 4,  4,  4,  3,  3}
            };
            IBookshelf grid = new TestBookshelf(template);

            assertEquals(18, adjacencyBonusGoal.evaluatePoints(grid));
        }

        @Test
        @DisplayName("weird shape")
        void evaluateOnWeirdShape() {
            int[][] template = new int[][] {
                    { 1,  1,  1, -1, -1},
                    { 2,  1,  1,  3, -1},
                    { 0,  2,  3,  2, -1},
                    { 4,  5,  3,  5,  3},
                    { 4,  4,  3,  3,  3},
                    { 4,  4,  4,  3,  3}
            };

            IBookshelf grid = new TestBookshelf(template);

            assertEquals(21, adjacencyBonusGoal.evaluatePoints(grid));
        }
    }
}
