package chess;

import java.util.Collection;

import layers.KingCacheBoard;
import movecalculators.LegalMoveCalculator;
import moveexecutors.Move;
import positioncaptures.Capturer;

/*
 * Necesitamos los estadios para seleccionar el LegalMoveCalculator que corresponde
 */

//TODO: La generacion de movimientos dummy debiera ser en base al layer de color. 
//      Me imagino un tablero con X y O para representar los distintos colores.
public class BoardAnalyzer {

	private BoardState boardState = null;
	
	private KingCacheBoard kingCacheBoard = null;	
	
	private boolean isKingInCheck = false;

	private Capturer capturer;
	
	private LegalMoveCalculator defaultMoveCalculator = null;
	
	private LegalMoveCalculator noCheckLegalMoveCalculator = null;		

	public boolean isKingInCheck() {
		return isKingInCheck;
	}
	
	public BoardStatus getBoardStatus() {
		this.isKingInCheck = calculateKingInCheck();
		
		boolean existsLegalMove = getMoveCalculator().existsLegalMove();
		
		BoardStatus result = new BoardStatus();
		result.setKingInCheck(isKingInCheck);
		result.setExistsLegalMove(existsLegalMove);

		return result;
	}
	
	public Collection<Move> getLegalMoves() {
		return getMoveCalculator().getLegalMoves();
	}	
	
	public LegalMoveCalculator getMoveCalculator() {
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

	public void setCapturer(Capturer capturer) {
		this.capturer = capturer;
	}
	

	public void setDefaultMoveCalculator(LegalMoveCalculator defaultMoveCalculator) {
		this.defaultMoveCalculator = defaultMoveCalculator;
	}

	public void setNoCheckLegalMoveCalculator(LegalMoveCalculator noCheckLegalMoveCalculator) {
		this.noCheckLegalMoveCalculator = noCheckLegalMoveCalculator;
	}	
	
	protected boolean calculateKingInCheck() {
		Color turnoActual = boardState.getTurnoActual();
		
		isKingInCheck = capturer.positionCaptured(turnoActual.opositeColor(), kingCacheBoard.getKingSquare(turnoActual));
		
		return isKingInCheck;
	}	

}
