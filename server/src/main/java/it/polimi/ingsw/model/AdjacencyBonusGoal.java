package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.GameTile;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class AdjacencyBonusGoal {
    private static final int MIN_GROUP_SIZE = 3;
    private static int[] pointsTable = { 2, 3, 5, 8 };

    public int evaluatePoints(Bookshelf bookshelf) throws NullPointerException {
        if (bookshelf == null) throw new NullPointerException();

        int points = 0;
        List<Integer> groups = getTileGroupsSizes(bookshelf);
        for(Integer group : groups) points += convertToPoints(group);

        return points;
    }

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

    private int floodFill(int startX, int startY, Bookshelf bookshelf, boolean[][] visited) {
        if(visited[startY][startX]) return 0;

        Tile[][] tiles = bookshelf.getTiles();
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

    private boolean[][] initVisited(Bookshelf bookshelf) {
        boolean[][] visited = new boolean[Bookshelf.BOOKSHELF_ROW][Bookshelf.BOOKSHELF_COLUMN];
        Tile[][] tiles = bookshelf.getTiles();

        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[i].length; j++) {
                boolean isEmptySpace = tiles[i][j] == null;
                visited[i][j] = isEmptySpace;
            }
        }

        return visited;
    }

    public int convertToPoints(int groupSize) {
        if (groupSize < MIN_GROUP_SIZE) return 0;

        // Truncate all groups that exceed 6 in size to be exactly 6
        int normalizedGroupSize = groupSize - MIN_GROUP_SIZE;
        int truncatedGroupSize = Integer.max(normalizedGroupSize, MIN_GROUP_SIZE);

        return pointsTable[truncatedGroupSize];
    }
}
