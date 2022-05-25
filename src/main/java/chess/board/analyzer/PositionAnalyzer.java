package chess.board.analyzer;

import chess.board.GameState;
import chess.board.movesgenerators.legal.LegalMoveGenerator;
import chess.board.moves.containers.MoveContainerReader;

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
	
	private CheckAndPinnedAnalyzer checkAndPinnedAnalyzer;
	
	private GameState gameState;
	
	private LegalMoveGenerator legalMoveGenerator;
	
	
	public GameState.GameStatus updateGameStatus() {
		AnalyzerResult analysis = analyze();

		MoveContainerReader legalMoves = legalMoveGenerator.getLegalMoves(analysis);
		
		boolean existsLegalMove = !legalMoves.isEmpty();
		
		GameState.GameStatus gameStatus = null;

		if (existsLegalMove) {
			if (analysis.isKingInCheck()) {
				gameStatus = GameState.GameStatus.CHECK;
			} else {
				gameStatus = GameState.GameStatus.IN_PROGRESS;
			}
		} else {
			if (analysis.isKingInCheck()) {
				gameStatus = GameState.GameStatus.MATE;
			} else {
				gameStatus = GameState.GameStatus.DRAW;
			}
		}

		gameState.setStatus(gameStatus);
		gameState.setAnalyzerResult(analysis);
		gameState.setLegalMoves(legalMoves);

		return gameStatus;
	}	
	
	public AnalyzerResult analyze() {
		
		AnalyzerResult result = new AnalyzerResult();
		
		checkAndPinnedAnalyzer.analyze();
		
		result.setKingInCheck(checkAndPinnedAnalyzer.isKingInCheck());
		
		result.setPinnedSquares(checkAndPinnedAnalyzer.getPinnedPositions());

		result.setPinnedPositionCardinals(checkAndPinnedAnalyzer.getPinnedPositionCardinals());
		
		return result;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public void setLegalMoveGenerator(LegalMoveGenerator legalMoveGenerator) {
		this.legalMoveGenerator = legalMoveGenerator;
	}

	public void setCheckAndPinnedAnalyzer(CheckAndPinnedAnalyzer checkAndPinnedAnalyzer) {
		this.checkAndPinnedAnalyzer = checkAndPinnedAnalyzer;
	}	

}
