package chess.board.analyzer;

import java.util.Collection;

import chess.board.Color;
import chess.board.GameState;
import chess.board.legalmovesgenerators.LegalMoveGenerator;
import chess.board.moves.Move;
import chess.board.position.ChessPositionReader;

/*
 * Necesitamos los estadios para seleccionar el LegalMoveGenerator que corresponde
 */

//TODO: La generacion de movimientos dummy debiera ser en base al layer de color. 
//      Me imagino un tablero con X y O para representar los distintos colores.

/**
 * @author Mauricio Coria
 *
 */
public class PositionAnalyzer {

	private ChessPositionReader positionReader;

	private Capturer capturer;
	
	private LegalMoveGenerator defaultMoveCalculator;
	
	private LegalMoveGenerator noCheckLegalMoveGenerator;
	
	private GameState gameState;
	
	
	public GameState.GameStatus updateGameStatus() {
		AnalyzerResult analyzerResult = getAnalyzerResult();
		Collection<Move> movimientosPosibles = analyzerResult.getLegalMoves();
		boolean existsLegalMove = !movimientosPosibles.isEmpty();
		
		GameState.GameStatus gameStatus = null;

		if (existsLegalMove) {
			if (analyzerResult.isKingInCheck()) {
				gameStatus = GameState.GameStatus.JAQUE;
			} else {
				gameStatus = GameState.GameStatus.IN_PROGRESS;
			}
		} else {
			if (analyzerResult.isKingInCheck()) {
				gameStatus = GameState.GameStatus.JAQUE_MATE;
			} else {
				gameStatus = GameState.GameStatus.TABLAS;
			}
		}

		gameState.setStatus(gameStatus);
		gameState.setAnalyzerResult(analyzerResult);

		return gameStatus;
	}	
	
	public AnalyzerResult getAnalyzerResult() {
		boolean isKingInCheck = calculateKingInCheck();
		AnalyzerResult result = new AnalyzerResult();
		result.setKingInCheck(isKingInCheck);
		result.setLegalMoves(getLegalMoves(isKingInCheck));
		return result;
	}
	
	protected boolean calculateKingInCheck() {
		Color turnoActual = positionReader.getTurnoActual();
		
		return capturer.positionCaptured(turnoActual.opositeColor(), positionReader.getKingSquare(turnoActual));
	}	
	
	protected Collection<Move> getLegalMoves(boolean isKingInCheck) {
		return getMoveCalculator(isKingInCheck).getLegalMoves();
	}	
	
	protected LegalMoveGenerator getMoveCalculator(boolean isKingInCheck) {
		if(! isKingInCheck){
			return noCheckLegalMoveGenerator;
		}
		return defaultMoveCalculator;
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

	public void setPositionReader(ChessPositionReader positionReader) {
		this.positionReader = positionReader;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}	

}
