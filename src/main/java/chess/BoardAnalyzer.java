package chess;

import positioncaptures.ImprovedCapturer;

/*
 * Necesitamos los estadios para seleccionar el LegalMoveCalculator que corresponde
 */
public class BoardAnalyzer {

	private Board board = null;
	
	private boolean isKingInCheck = false;

	private ImprovedCapturer capturer;

	public BoardAnalyzer(Board board) {
		this.board = board;
		this.capturer = new ImprovedCapturer(board.getDummyBoard());
	}

	public void analyze() {
		this.isKingInCheck = calculateKingInCheck();
	}	

	public boolean isKingInCheck() {
		return isKingInCheck;
	}
	
	private boolean calculateKingInCheck() {
		Color turnoActual = board.getBoardState().getTurnoActual();
		
		isKingInCheck = capturer.positionCaptured(turnoActual.opositeColor(), board.getKingCacheBoard().getKingSquare(turnoActual));
		
		return isKingInCheck;
	}	

}
