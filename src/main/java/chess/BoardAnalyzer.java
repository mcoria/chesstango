package chess;

import java.util.Collection;

import chess.legalmovesgenerators.LegalMoveGenerator;
import chess.moves.Move;
import chess.position.KingCacheBoard;
import chess.position.PositionState;
import chess.positioncaptures.Capturer;

/*
 * Necesitamos los estadios para seleccionar el LegalMoveGenerator que corresponde
 */

//TODO: La generacion de movimientos dummy debiera ser en base al layer de color. 
//      Me imagino un tablero con X y O para representar los distintos colores.

/**
 * @author Mauricio Coria
 *
 */
public class BoardAnalyzer {

	private PositionState positionState = null;
	
	private KingCacheBoard kingCacheBoard = null;	
	
	private boolean isKingInCheck = false;

	private Capturer capturer;
	
	private LegalMoveGenerator defaultMoveCalculator = null;
	
	private LegalMoveGenerator noCheckLegalMoveGenerator = null;		

	public boolean isKingInCheck() {
		return isKingInCheck;
	}
	
	public BoardStatus getBoardStatus() {
		this.isKingInCheck = calculateKingInCheck();
		
		//boolean existsLegalMove = getMoveCalculator().existsLegalMove();
		
		BoardStatus result = new BoardStatus();
		result.setKingInCheck(isKingInCheck);
		result.setLegalMoves(getLegalMoves());
		//result.setExistsLegalMove(existsLegalMove);

		return result;
	}
	
	public Collection<Move> getLegalMoves() {
		return getMoveCalculator().getLegalMoves();
	}	
	
	public LegalMoveGenerator getMoveCalculator() {
		if(! isKingInCheck() ){
			return noCheckLegalMoveGenerator;
		}
		return defaultMoveCalculator;
	}	

	public void setBoardState(PositionState positionState) {
		this.positionState = positionState;
	}

	public void setKingCacheBoard(KingCacheBoard kingCacheBoard) {
		this.kingCacheBoard = kingCacheBoard;
	}

	public void setCapturer(Capturer capturer) {
		this.capturer = capturer;
	}
	

	public void setDefaultMoveCalculator(LegalMoveGenerator defaultMoveCalculator) {
		this.defaultMoveCalculator = defaultMoveCalculator;
	}

	public void setNoCheckLegalMoveGenerator(LegalMoveGenerator noCheckLegalMoveGenerator) {
		this.noCheckLegalMoveGenerator = noCheckLegalMoveGenerator;
	}	
	
	protected boolean calculateKingInCheck() {
		Color turnoActual = positionState.getTurnoActual();
		
		isKingInCheck = capturer.positionCaptured(turnoActual.opositeColor(), kingCacheBoard.getKingSquare(turnoActual));
		
		return isKingInCheck;
	}	

}
