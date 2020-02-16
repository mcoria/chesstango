package movegenerators;

import java.util.Collection;

import chess.Move;

@FunctionalInterface
public interface MoveFilter {
	void filterMove(Collection<Move> moves, Move move);
}
