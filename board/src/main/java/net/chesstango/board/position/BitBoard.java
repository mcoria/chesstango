package net.chesstango.board.position;

/**
 * Bit centric board representation
 *
 * @author Mauricio Coria
 */
public interface BitBoard extends BitBoardReader, BitBoardWriter {

    void init(SquareBoardReader board);
}
