package chess.analyzer;

import java.util.Collection;

import chess.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public class AnalyzerResult {
	private boolean isKingInCheck;
	private Collection<Move> legalMoves;

	public boolean isKingInCheck() {
		return isKingInCheck;

	}

	public void setKingInCheck(boolean isKingInCheck) {
		this.isKingInCheck = isKingInCheck;
	}

	public Collection<Move> getLegalMoves() {
		return legalMoves;
	}

	public void setLegalMoves(Collection<Move> legalMoves) {
		this.legalMoves = legalMoves;
	}

	public boolean isExistsLegalMove() {
		return !legalMoves.isEmpty();
	}
}
