package chess;

import layers.KingCacheBoard;
import movecalculators.DefaultLegalMoveCalculator;
import movecalculators.LegalMoveCalculator;
import movecalculators.NoCheckLegalMoveCalculator;
import positioncaptures.ImprovedCapturer;

/*
 * Necesitamos los estadios para seleccionar el LegalMoveCalculator que corresponde
 */
public class BoardAnalyzer {

	private BoardState boardState = null;
	
	private KingCacheBoard kingCacheBoard = null;	
	
	private boolean isKingInCheck = false;

	private ImprovedCapturer capturer;
	
	private DefaultLegalMoveCalculator defaultMoveCalculator = null;
	
	private NoCheckLegalMoveCalculator noCheckLegalMoveCalculator = null;	

	public void analyze() {
		this.isKingInCheck = calculateKingInCheck();
	}	

	public boolean isKingInCheck() {
		return isKingInCheck;
	}
	
	private boolean calculateKingInCheck() {
		Color turnoActual = boardState.getTurnoActual();
		
		isKingInCheck = capturer.positionCaptured(turnoActual.opositeColor(), kingCacheBoard.getKingSquare(turnoActual));
		
		return isKingInCheck;
	}
	
	LegalMoveCalculator getMoveCalculator() {
		if(! isKingInCheck() ){
			return noCheckLegalMoveCalculator;
		}
		return defaultMoveCalculator;
	}	

	public void setBoardState(BoardState boardState) {
		this.boardState = boardState;
	}

	public void setKingCacheBoard(KingCacheBoard kingCacheBoard) {
		this.kingCacheBoard = kingCacheBoard;
	}

	public void setCapturer(ImprovedCapturer capturer) {
		this.capturer = capturer;
	}
	

	public void setDefaultMoveCalculator(DefaultLegalMoveCalculator defaultMoveCalculator) {
		this.defaultMoveCalculator = defaultMoveCalculator;
	}

	public void setNoCheckLegalMoveCalculator(NoCheckLegalMoveCalculator noCheckLegalMoveCalculator) {
		this.noCheckLegalMoveCalculator = noCheckLegalMoveCalculator;
	}		

}
