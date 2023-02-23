package net.chesstango.board.analyzer;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;

import java.util.AbstractMap;
import java.util.List;

/**
 * @author Mauricio Coria
 *
 */
public class AnalyzerResult {
	private boolean isKingInCheck;
	private long pinnedSquares;

	private List<AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal>> pinnedPositionCardinals;

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

	public List<AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal>> getPinnedPositionCardinals() {
		return pinnedPositionCardinals;
	}

	public void setPinnedPositionCardinals(List<AbstractMap.SimpleImmutableEntry<PiecePositioned, Cardinal>> pinnedPositionCardinals) {
		this.pinnedPositionCardinals = pinnedPositionCardinals;
	}

}
