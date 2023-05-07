package it.polimi.ingsw.client.view.cli.graphics.board;

/**
 * Type of tile drawn on the board.
 * BLOCKED - Unplayable tile;
 * THREE_PLAYERS - Tile that requires 3 players;
 * FOUR_PLAYERS - Tile that requires 4 players;
 */
public enum BoardTileType {
    THREE_PLAYERS,
    FOUR_PLAYERS,
    BLOCKED
}
