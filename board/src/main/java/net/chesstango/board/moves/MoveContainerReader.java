package net.chesstango.board.moves;

import net.chesstango.board.Piece;
import net.chesstango.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveContainerReader extends Iterable<Move> {
    int size();

    boolean isEmpty();

    boolean contains(Move move);

    Move getMove(Square from, Square to);
    Move getMove(Square from, Square to, Piece promotionPiece);
}
