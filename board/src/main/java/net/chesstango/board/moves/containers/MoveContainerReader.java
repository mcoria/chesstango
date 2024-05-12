package net.chesstango.board.moves.containers;

import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveContainerReader<T extends Move> extends Iterable<T> {
    int size();

    boolean isEmpty();

    boolean contains(Move move);

    T getMove(Square from, Square to);
    T getMove(Square from, Square to, Piece promotionPiece);

    boolean hasQuietMoves();


}
