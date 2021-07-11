package chess;

import java.util.Collection;

public class Game {
	
	public static enum GameStatus {
		IN_PROGRESS,
		JAQUE,
		JAQUE_MATE, 
		TABLAS		
	}
	
	private Board board;
	
	private GameStack boardPila = new GameStack();
	
	public Game(Board tablero){
		this.board = tablero;
		updateGameStatus();
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
		//assert(boardPila.getMovimientosPosibles().contains(move));
		
		boardPila.setMovimientoSeleccionado(move);
		
		boardPila.push();
		
		board.execute(move);
		
		return updateGameStatus();
	}


	public GameStatus undoMove() {
		boardPila.pop();
		
		Move lastMove = boardPila.getMovimientoSeleccionado();
		
		board.undo(lastMove);
		
		return getGameStatus();
	}
	

	protected GameStatus updateGameStatus() {
		BoardStatus boardStatus = board.getBoardStatus();
		boolean existsLegalMove = boardStatus.isExistsLegalMove();
		GameStatus gameStatus = null;

		if (existsLegalMove) {
			if (boardStatus.isKingInCheck()) {
				gameStatus = GameStatus.JAQUE;
			} else {
				gameStatus = GameStatus.IN_PROGRESS;
			}
		} else {
			if (boardStatus.isKingInCheck()) {
				gameStatus = GameStatus.JAQUE_MATE;
			} else {
				gameStatus = GameStatus.TABLAS;
			}
		}

		boardPila.setStatus(gameStatus);

		return gameStatus;
	}
	
	protected Move getMovimiento(Square from, Square to) {
		for (Move move : getMovimientosPosibles() ) {
			if(from.equals(move.getFrom().getKey()) && to.equals(move.getTo().getKey())){
				return move;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return board.toString();
	}

	public final Board getTablero() {
		return board;
	}

	public final Collection<Move> getMovimientosPosibles() {
		if(boardPila.getMovimientosPosibles() == null){
			boardPila.setMovimientosPosibles(board.getLegalMoves());
		}
		return boardPila.getMovimientosPosibles();
	}

	public final Color getTurnoActual() {
		return board.getBoardState().getTurnoActual();
	}

	public final GameStatus getGameStatus() {
		return boardPila.getStatus();
	}

}
