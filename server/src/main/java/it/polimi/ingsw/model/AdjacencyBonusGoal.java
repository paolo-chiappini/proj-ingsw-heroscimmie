package it.polimi.ingsw.model;

import it.polimi.ingsw.model.interfaces.IBookshelf;
import it.polimi.ingsw.util.BookshelfFloodFill;

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
        List<Integer> groups = BookshelfFloodFill.getTileGroupsSizes(bookshelf);
        for(Integer group : groups) points += convertToPoints(group);

        return points;
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
