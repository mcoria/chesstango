package chess.board.analyzer;

/**
 * @author Mauricio Coria
 *
 */
public class AnalyzerResult {
	private boolean isKingInCheck;

	public boolean isKingInCheck() {
		return isKingInCheck;

	}

	public void setKingInCheck(boolean isKingInCheck) {
		this.isKingInCheck = isKingInCheck;
	}

}
