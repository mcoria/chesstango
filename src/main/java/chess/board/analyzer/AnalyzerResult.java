package chess.board.analyzer;

/**
 * @author Mauricio Coria
 *
 */
public class AnalyzerResult {
	private boolean isKingInCheck;
	
	private long pinnedSquares;

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

}
