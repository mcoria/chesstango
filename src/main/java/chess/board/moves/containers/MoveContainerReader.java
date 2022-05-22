package chess.board.moves.containers;

import chess.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveContainerReader extends Iterable<Move> {
    int size();

    boolean isEmpty();

    boolean contains(Move simpleMove);
}
