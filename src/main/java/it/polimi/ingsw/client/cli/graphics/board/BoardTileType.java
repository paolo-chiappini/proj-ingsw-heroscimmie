package it.polimi.ingsw.client.cli.graphics.board;

/**
 * Type of tile drawn on the board.
 * BLOCKED - Unplayable tile;
 * THREE_PLAYERS - Tile that requires 3 players;
 * FOUR_PLAYERS - Tile that requires 4 players;
 */
public enum BoardTileType {
    BLOCKED,
    THREE_PLAYERS,
    FOUR_PLAYERS
}
