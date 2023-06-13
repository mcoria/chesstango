package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 *
 */
public interface KingSquare extends KingSquareReader, KingSquareWriter {
    void init(SquareBoardReader board);

}
