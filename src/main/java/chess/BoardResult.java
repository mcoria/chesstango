package chess;

import java.util.Collection;

public class BoardResult {
	
	private boolean isKingInCheck;
	
	private Collection<Move> legalMoves;

	public Collection<Move> getLegalMoves() {
		return legalMoves;
		
	}

	public boolean isKingInCheck() {
		return isKingInCheck;

	}

	public void setKingInCheck(boolean isKingInCheck) {
		this.isKingInCheck = isKingInCheck;
	}

	public void setLegalMoves(Collection<Move> legalMoves) {
		this.legalMoves = legalMoves;
	}	
}
