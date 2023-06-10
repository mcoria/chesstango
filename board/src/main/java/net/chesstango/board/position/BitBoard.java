package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 *
 */
public interface BitBoard extends BitBoardReader, BitBoardWriter {

    void init(SquareBoardReader board);
}
