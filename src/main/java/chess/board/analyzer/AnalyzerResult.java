package chess.board.analyzer;

import chess.board.Square;
import chess.board.iterators.Cardinal;

import java.util.AbstractMap;
import java.util.List;

/**
 * @author Mauricio Coria
 *
 */
public class AnalyzerResult {
	private boolean isKingInCheck;
	
	private long pinnedSquares;

	private List<AbstractMap.SimpleImmutableEntry<Square, Cardinal>> pinnedPositionCardinals;

	public boolean isKingInCheck() {
		return isKingInCheck;
	}

	public void setKingInCheck(boolean isKingInCheck) {
		this.isKingInCheck = isKingInCheck;
	}

	public long getPinnedSquares() {
		return pinnedSquares;
	}

	public void setPinnedSquares(long pinnedSquares) {
		this.pinnedSquares = pinnedSquares;
	}

	public List<AbstractMap.SimpleImmutableEntry<Square, Cardinal>> getPinnedPositionCardinals() {
		return pinnedPositionCardinals;
	}

	public void setPinnedPositionCardinals(List<AbstractMap.SimpleImmutableEntry<Square, Cardinal>> pinnedPositionCardinals) {
		this.pinnedPositionCardinals = pinnedPositionCardinals;
	}

	public Cardinal getThreatDirection(Square square) {
		return pinnedPositionCardinals.stream().filter(entry -> entry.getKey().equals(square)).findFirst().get().getValue();
	}

}
