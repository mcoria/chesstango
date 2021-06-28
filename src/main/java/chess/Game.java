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
		Collection<Move> movimientosPosibles = boardStatus.getLegalMoves();
		GameStatus gameStatus = null;
		
		if(movimientosPosibles.isEmpty()){
			if( boardStatus.isKingInCheck() ){
				gameStatus = GameStatus.JAQUE_MATE;
			} else {
				gameStatus = GameStatus.TABLAS;
			}
		} else {
			if( boardStatus.isKingInCheck() ){
				gameStatus = GameStatus.JAQUE;
			} else {
				gameStatus = GameStatus.IN_PROGRESS;
			}			
		}
		
		boardPila.setMovimientosPosibles(movimientosPosibles);
		boardPila.setStatus(gameStatus);
		
		return gameStatus;
	}
	
	protected Move getMovimiento(Square from, Square to) {
		for (Move move : boardPila.getMovimientosPosibles()) {
			if(from.equals(move.getFrom().getKey()) && to.equals(move.getTo().getKey())){
				return move;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(board.toString());
		buffer.append("Turno: " + getTurnoActual() + "\n");
		return buffer.toString();
	}

	public final Board getTablero() {
		return board;
	}

	public final Collection<Move> getMovimientosPosibles() {
		return boardPila.getMovimientosPosibles();
	}

	public final Color getTurnoActual() {
		return board.getBoardState().getTurnoActual();
	}

	public final GameStatus getGameStatus() {
		return boardPila.getStatus();
	}

}
