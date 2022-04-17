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
	
	private GameState gameState;
	
	
	private LegalMoveGenerator legalMoveGenerator;
	
	
	public GameState.GameStatus updateGameStatus() {
		AnalyzerResult analysis = getAnalyzerResult();
		Collection<Move> legalMoves = legalMoveGenerator.getLegalMoves(analysis);
		boolean existsLegalMove = !legalMoves.isEmpty();
		
		GameState.GameStatus gameStatus = null;

		if (existsLegalMove) {
			if (analysis.isKingInCheck()) {
				gameStatus = GameState.GameStatus.JAQUE;
			} else {
				gameStatus = GameState.GameStatus.IN_PROGRESS;
			}
		} else {
			if (analysis.isKingInCheck()) {
				gameStatus = GameState.GameStatus.JAQUE_MATE;
			} else {
				gameStatus = GameState.GameStatus.TABLAS;
			}
		}

		gameState.setStatus(gameStatus);
		gameState.setAnalyzerResult(analysis);
		gameState.setLegalMoves(legalMoves);

		return gameStatus;
	}	
	
	public AnalyzerResult getAnalyzerResult() {
		AnalyzerResult result = new AnalyzerResult();
		result.setKingInCheck(calculateKingInCheck());
		return result;
	}
	
	protected boolean calculateKingInCheck() {
		Color turnoActual = positionReader.getTurnoActual();
		
		return capturer.positionCaptured(turnoActual.opositeColor(), positionReader.getKingSquare(turnoActual));
	}

	public void setCapturer(Capturer capturer) {
		this.capturer = capturer;
	}

	public void setPositionReader(ChessPositionReader positionReader) {
		this.positionReader = positionReader;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public void setLegalMoveGenerator(LegalMoveGenerator legalMoveGenerator) {
		this.legalMoveGenerator = legalMoveGenerator;
	}	

}
