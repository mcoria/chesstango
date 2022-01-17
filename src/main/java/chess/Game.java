package chess;

import java.util.Collection;

import chess.analyzer.PositionAnalyzer;
import chess.analyzer.AnalyzerResult;
import chess.moves.Move;
import chess.position.ChessPosition;

/**
 * @author Mauricio Coria
 *
 */
public class Game {
	
	public static enum GameStatus {
		IN_PROGRESS,
		JAQUE,
		JAQUE_MATE, 
		TABLAS		
	}
	
	private ChessPosition chessPosition;
	
	private GameStack boardPila = new GameStack();
	
	private PositionAnalyzer analyzer = null;	
	
	public Game(ChessPosition tablero){
		this.chessPosition = tablero;
	}

	public GameStatus executeMove(Square from, Square to) {
		if (GameStatus.IN_PROGRESS.equals(boardPila.getStatus()) || GameStatus.JAQUE.equals(boardPila.getStatus())) {
			Move move = getMovimiento(from, to);
			if (move != null) {
				return executeMove(move);
			} else {
				throw new RuntimeException("Invalid move: " + from.toString() + " " + to.toString());
			}
		} else {
			throw new RuntimeException("Invalid game state");
		}
	}
	

	public GameStatus executeMove(Move move) {
		//assert(boardPila.getPossibleMoves().contains(move));
		
		boardPila.setMovimientoSeleccionado(move);
		
		boardPila.push();
		
		chessPosition.execute(move);
		
		return updateGameStatus();
	}


	public GameStatus undoMove() {
		boardPila.pop();
		
		Move lastMove = boardPila.getMovimientoSeleccionado();
		
		chessPosition.undo(lastMove);
		
		return getGameStatus();
	}
	

	protected GameStatus updateGameStatus() {
		AnalyzerResult analyzerResult = analyzer.analyze();
		Collection<Move> movimientosPosibles = analyzerResult.getLegalMoves();
		boolean existsLegalMove = !movimientosPosibles.isEmpty();
		GameStatus gameStatus = null;

		if (existsLegalMove) {
			if (analyzerResult.isKingInCheck()) {
				gameStatus = GameStatus.JAQUE;
			} else {
				gameStatus = GameStatus.IN_PROGRESS;
			}
		} else {
			if (analyzerResult.isKingInCheck()) {
				gameStatus = GameStatus.JAQUE_MATE;
			} else {
				gameStatus = GameStatus.TABLAS;
			}
		}

		boardPila.setStatus(gameStatus);
		boardPila.setAnalyzerResult(analyzerResult);

		return gameStatus;
	}
	
	public Move getMovimiento(Square from, Square to) {
		for (Move move : getPossibleMoves() ) {
			if(from.equals(move.getFrom().getKey()) && to.equals(move.getTo().getKey())){
				return move;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return chessPosition.toString();
	}

	public ChessPosition getTablero() {
		return chessPosition;
	}

	public Collection<Move> getPossibleMoves() {
		return boardPila.getAnalyzerResult().getLegalMoves();
	}

	public Color getTurnoActual() {
		return chessPosition.getBoardState().getTurnoActual();
	}

	public GameStatus getGameStatus() {
		return boardPila.getStatus();
	}
	
	public void setAnalyzer(PositionAnalyzer analyzer) {
		this.analyzer = analyzer;
		updateGameStatus();
	}
}
