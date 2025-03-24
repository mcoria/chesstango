package net.chesstango.board.moves.containers;

import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 */
public interface MoveContainerReader<M extends Move> extends Iterable<M> {
    int size();

    boolean isEmpty();

    boolean contains(M move);

    M getMove(Square from, Square to);

    M getMove(Square from, Square to, Piece promotionPiece);

    boolean hasQuietMoves();
}
