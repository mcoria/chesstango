package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 *
 */
public interface ZobristHashReader {
    long getZobristHash();
    long getZobristEnPassantSquare();
}
