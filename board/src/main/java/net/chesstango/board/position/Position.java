package net.chesstango.board.position;

/**
 *
 * Interface representing a chess position.
 * This interface extends both ChessPositionReader and ChessPositionWriter,
 * indicating that it provides methods for reading and writing chess positions.
 *
 *
 * Chess position representation:
 * <ul>
 * <li>Piece placement on the Board Representation</li>
 * <li>Side to move</li>
 * <li>Castling Rights</li>
 * <li>En passant target square</li>
 * <li>Halfmove Clock</li>
 * </ul>
 *
 * @author Mauricio Coria
 */
public interface Position extends PositionReader, PositionWriter {
    /**
     * Initializes the chess position.
     * This method should set up the initial state of the chess position,
     */
    void init();
}
