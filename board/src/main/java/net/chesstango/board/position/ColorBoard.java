package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 *
 */
public interface ColorBoard extends ColorBoardReader, ColorBoardWriter {

    void init(SquareBoardReader board);
}
