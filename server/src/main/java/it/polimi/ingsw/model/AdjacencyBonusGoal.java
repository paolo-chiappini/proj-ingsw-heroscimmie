package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.GameTile;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * This class implements the logic necessary to calculate how many
 * points a player has scored based on the adjacency bonuses only.
 */
public class AdjacencyBonusGoal {
    private static final int MIN_GROUP_SIZE = 3;
    private static int[] pointsTable = { 2, 3, 5, 8 };

    /**
     * The method evaluates the amount of points scored based on the bonus.
     * @param bookshelf bookshelf containing the tiles to evaluate.
     * @return amount of points scored.
     * @throws NullPointerException thrown if bookshelf is null.
     */
    public int evaluatePoints(Bookshelf bookshelf) throws NullPointerException {
        if (bookshelf == null) throw new NullPointerException();

        int points = 0;
        List<Integer> groups = getTileGroupsSizes(bookshelf);
        for(Integer group : groups) points += convertToPoints(group);

        return points;
    }

    /**
     * The method finds all the groups of adjacent tiles contained in bookshelf and
     * returns their size.
     * @param bookshelf bookshelf to evaluate.
     * @return list containing the size of each adjacency group inside bookshelf.
     */
    private List<Integer> getTileGroupsSizes(Bookshelf bookshelf) {
        boolean[][] visited = new boolean[0][0];
        List<Integer> groups = new ArrayList<>();

        visited = initVisited(bookshelf);
        for (int y = 0; y < Bookshelf.BOOKSHELF_ROW; y++) {
            for (int x = 0; x < Bookshelf.BOOKSHELF_COLUMN; x++) {
                int groupSize = floodFill(x, y, bookshelf, visited);
                if(groupSize > 0) groups.add(groupSize);
            }
        }

        return groups;
    }

    /**
     * The method uses a classic flood-fill algorithm to find a single
     * group of adjacent tiles of the same type and returns its size.
     * @param startX x position to start filling.
     * @param startY y position to start filling.
     * @param bookshelf bookshelf to "fill" (search adjacency groups).
     * @param visited flags marking the groups that have already been filled.
     * @return the size of the group filled.
     */
    private int floodFill(int startX, int startY, Bookshelf bookshelf, boolean[][] visited) {
        if(visited[startY][startX]) return 0;

        GameTile[][] tiles = bookshelf.getTiles();
        GameTile startTile = tiles[startY][startX];

        int x, y, xEast, xWest, yNorth, ySouth;
        int width, height;
        int groupDimension = 0;
        Queue<Integer> coords = new PriorityQueue<>();

        width = Bookshelf.BOOKSHELF_COLUMN;
        height = Bookshelf.BOOKSHELF_ROW;
        coords.add(startX);
        coords.add(startY);

        while (coords.size() > 0) {
            x = coords.poll();
            y = coords.poll();
            xWest = x;
            xEast = x;

            if (y > 0) yNorth = y - 1;
            else yNorth = -1;
            if (y < height - 1) ySouth = y + 1;
            else ySouth = -1;

            while (xEast < width - 1 && tiles[y][xEast + 1].equals(startTile)) {
                xEast++;
            }

            while (xWest > 0 && tiles[y][xWest - 1].equals(startTile)) {
                xWest--;
            }

            for (x = xWest; x < xEast; x++) {
                visited[x][y] = true;
                if (yNorth >= 0 && tiles[yNorth][x].equals(startTile)) {
                    coords.add(x);
                    coords.add(yNorth);
                }
                if (ySouth >= 0 && tiles[ySouth][x].equals(startTile)) {
                    coords.add(x);
                    coords.add(ySouth);
                }
                groupDimension++;
            }
        }

        return groupDimension;
    }

    /**
     * This method initializes a 2D array of flags used by the flood-fill algorithm to
     * find groups of tiles.
     * @param bookshelf bookshelf used for initializing the visited flags.
     * @return an array of flags that assume the value true if the corresponding
     *      space inside the bookshelf is empty.
     */
    private boolean[][] initVisited(Bookshelf bookshelf) {
        boolean[][] visited = new boolean[Bookshelf.BOOKSHELF_ROW][Bookshelf.BOOKSHELF_COLUMN];
        GameTile[][] tiles = bookshelf.getTiles();

        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[i].length; j++) {
                boolean isEmptySpace = tiles[i][j] == null;
                visited[i][j] = isEmptySpace;
            }
        }

        return visited;
    }

    /**
     * The method converts the size of a group of tiles in the corresponding
     * amount of points.
     * @param groupSize size of the group to convert into points.
     * @return amount of points corresponding to the size of the group.
     *      <p>If the group is too small, the awarded points will be 0.
     *      <p>If the group is larger than a certain threshold, the points awarded will
     *      be capped at a maximum amount.
     *      <p>If the group's size is between the minimum and the threshold (both included),
     *      the amount of points to award will be chosen based on a points table.
     */
    public int convertToPoints(int groupSize) {
        if (groupSize < MIN_GROUP_SIZE) return 0;

        // Truncate all groups that exceed 6 in size to be exactly 6
        int normalizedGroupSize = groupSize - MIN_GROUP_SIZE;
        int truncatedGroupSize = Integer.max(normalizedGroupSize, MIN_GROUP_SIZE);

        return pointsTable[truncatedGroupSize];
    }
}
