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
	private long capturedPositions;
	private long safeKingPositions;

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

	public void setCapturedPositions(long capturedPositions) {
		this.capturedPositions = capturedPositions;
	}

	public long getCapturedPositions() {
		return capturedPositions;
	}

	public long getSafeKingPositions() {
		return safeKingPositions;
	}

	public void setSafeKingPositions(long safeKingPositions) {
		this.safeKingPositions = safeKingPositions;
	}
}
