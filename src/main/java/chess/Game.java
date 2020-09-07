package chess;

import java.util.Collection;

public class Game {
	
	public static enum GameStatus {
		IN_PROGRESS,
		JAQUE,
		JAQUE_MATE, 
		TABLAS		
	}
	
	private Board tablero;
	
	private GameStack boardPila = new GameStack();
	
	public Game(Board tablero){
		this.tablero = tablero;
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
		
		tablero.execute(move);
		
		return updateGameStatus();
	}


	public GameStatus undoMove() {
		boardPila.pop();
		
		Move lastMove = boardPila.getMovimientoSeleccionado();
		
		tablero.undo(lastMove);
		
		return getGameStatus();
	}
	
	protected GameStatus updateGameStatus() {
		BoardResult result = tablero.getBoardResult();
		Collection<Move> movimientosPosibles = result.getLegalMoves();
		GameStatus status = null;
		
		if(movimientosPosibles.isEmpty()){
			if( result.isKingInCheck() ){
				status = GameStatus.JAQUE_MATE;
			} else {
				status = GameStatus.TABLAS;
			}
		} else {
			if( result.isKingInCheck() ){
				status = GameStatus.JAQUE;
			} else {
				status = GameStatus.IN_PROGRESS;
			}			
		}
		
		boardPila.setMovimientosPosibles(movimientosPosibles);
		boardPila.setStatus(status);
		
		return status;
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
		buffer.append(tablero.toString());
		buffer.append("Turno: " + getTurnoActual() + "\n");
		return buffer.toString();
	}

	public final Board getTablero() {
		return tablero;
	}

	public final Collection<Move> getMovimientosPosibles() {
		return boardPila.getMovimientosPosibles();
	}

	public final Color getTurnoActual() {
		return tablero.getBoardState().getTurnoActual();
	}

	public final GameStatus getGameStatus() {
		return boardPila.getStatus();
	}

}
