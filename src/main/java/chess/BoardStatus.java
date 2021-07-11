package chess;

public class BoardStatus {
	
	private boolean isKingInCheck;
	private boolean existsLegalMove;
	

	public boolean isKingInCheck() {
		return isKingInCheck;

	}

	public void setKingInCheck(boolean isKingInCheck) {
		this.isKingInCheck = isKingInCheck;
	}

	public boolean isExistsLegalMove() {
		return existsLegalMove;
	}

	public void setExistsLegalMove(boolean existsLegalMove) {
		this.existsLegalMove = existsLegalMove;
	}

}
