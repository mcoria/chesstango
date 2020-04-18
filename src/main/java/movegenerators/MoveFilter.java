package movegenerators;

import chess.Move;

@FunctionalInterface
public interface MoveFilter {
	boolean filterMove(Move move);
}
