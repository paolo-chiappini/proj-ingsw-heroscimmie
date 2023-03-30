package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.IBookshelf;

import java.util.*;

/**
 * This class implements the logic necessary to calculate how many
 * points a player has scored based on the adjacency bonuses only.
 */
public class AdjacencyBonusGoal {
    private static final int MIN_GROUP_SIZE = 3;
    private static final int[] pointsTable = { 2, 3, 5, 8 };

    /**
     * The method evaluates the amount of points scored based on the bonus.
     * @param bookshelf bookshelf containing the tiles to evaluate.
     * @return amount of points scored.
     * @throws NullPointerException thrown if bookshelf is null.
     */
    public int evaluatePoints(IBookshelf bookshelf) throws NullPointerException {
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
    private List<Integer> getTileGroupsSizes(IBookshelf bookshelf) {
        boolean[][] visited;
        List<Integer> groups = new ArrayList<>();

        visited = initVisited(bookshelf);
        for (int y = 0; y < bookshelf.getHeight(); y++) {
            for (int x = 0; x < bookshelf.getWidth(); x++) {
                int groupSize = floodFill(x, y, bookshelf, visited);
                if(groupSize > 0) groups.add(groupSize);
            }
        }

        return groups;
    }

    /**
     * The method uses a classic flood-fill algorithm to find a single
     * group of adjacent tiles of the same type and returns its size.
     * (source: https://en.wikipedia.org/wiki/Flood_fill)
     * @param startX x position to start filling.
     * @param startY y position to start filling.
     * @param bookshelf bookshelf to "fill" (search adjacency groups).
     * @param visited flags marking the groups that have already been filled.
     * @return the size of the group filled.
     */
    private int floodFill(int startX, int startY, IBookshelf bookshelf, boolean[][] visited) {
        if(visited[startY][startX] || bookshelf.getTileAt(startY,startX) == null) return 0;

        TileType startTile = bookshelf.getTileAt(startY, startX).getType();
        int x, y, xEast, xWest, yNorth, ySouth;
        int width, height;
        int groupDimension = 0;
        Deque<Integer> coords = new ArrayDeque<>();

        width = bookshelf.getWidth();
        height = bookshelf.getHeight();
        coords.addLast(startX);
        coords.addLast(startY);

        while (coords.size() > 0) {
            x = coords.poll();
            y = coords.poll();
            if(visited[y][x]) continue;

            visited[y][x] = true;
            groupDimension++;

            xWest = x;
            xEast = x;

            if (y > 0) yNorth = y - 1;
            else yNorth = -1;
            if (y < height - 1) ySouth = y + 1;
            else ySouth = -1;

            while (xEast < width - 1 &&
                    bookshelf.getTileAt(y, xEast + 1) != null &&
                    bookshelf.getTileAt(y, xEast + 1).getType().equals(startTile)) {
                xEast++;
                visited[y][xEast] = true;
                groupDimension++;
            }

            while (xWest > 0 &&
                    bookshelf.getTileAt(y, xWest - 1) != null &&
                    bookshelf.getTileAt(y, xWest - 1).getType().equals(startTile)) {
                xWest--;
                visited[y][xWest] = true;
                groupDimension++;
            }

            for (x = xWest; x <= xEast; x++) {
                if (yNorth >= 0 &&
                        !visited[yNorth][x] &&
                        bookshelf.getTileAt(yNorth, x) != null &&
                        bookshelf.getTileAt(yNorth, x).getType().equals(startTile)
                ) {
                    coords.addLast(x);
                    coords.addLast(yNorth);
                }
                if (ySouth >= 0 &&
                        !visited[ySouth][x] &&
                        bookshelf.getTileAt(ySouth, x) != null &&
                        bookshelf.getTileAt(ySouth, x).getType().equals(startTile)
                ) {
                    coords.addLast(x);
                    coords.addLast(ySouth);
                }
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
    private boolean[][] initVisited(IBookshelf bookshelf) {
        boolean[][] visited = new boolean[bookshelf.getHeight()][bookshelf.getWidth()];

        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[i].length; j++) {
                boolean isEmptySpace = bookshelf.getTileAt(i, j) == null;
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
        int truncatedGroupSize = Integer.min(normalizedGroupSize, MIN_GROUP_SIZE);

        return pointsTable[truncatedGroupSize];
    }
}
