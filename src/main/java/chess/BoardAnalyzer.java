package chess;

public class BoardAnalyzer {
	
	private Board board = null;
	
	private boolean isKingInCheck = false;
	
	protected IsPositionCaptured positionCaptured = (Square square) -> false;

	public BoardAnalyzer(Board board, IsPositionCaptured positionCaptured) {
		this.board = board;
		this.positionCaptured = positionCaptured;
	}

	public void analyze() {
		this.isKingInCheck = calculateKingInCheck();
	}
	
	private boolean calculateKingInCheck() {
		isKingInCheck = positionCaptured.check(board.getKingSquare());
		return isKingInCheck;
	}	
	
	public boolean isKingInCheck() {
		return isKingInCheck;
	}		
	
}
