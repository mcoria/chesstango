package net.chesstango.board.moves;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveContainerReader extends Iterable<Move> {
    int size();

    boolean isEmpty();

    boolean contains(Move move);
}
