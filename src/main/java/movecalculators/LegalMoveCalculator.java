package movecalculators;

import java.util.Collection;

import chess.BoardAnalyzer;
import chess.Move;

public interface LegalMoveCalculator {

	Collection<Move> getLegalMoves(BoardAnalyzer analyzer);

}