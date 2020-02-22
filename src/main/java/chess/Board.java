package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Board {
	private DummyBoard tablero;
	
	private BoardState boardState;
	
	private BoardPila boardPila = new BoardPila();
	
	public Board(DummyBoard tablero){
		this.tablero = tablero;
		this.boardState = tablero.getBoardState();
		updateGameStatus();
	}

	public GameStatus executeMove(Square from, Square to) {
		if(GameStatus.IN_PROGRESS.equals(boardPila.getStatus())){
			Move move = getMovimiento(from, to);
			if(move != null) {
				return executeMove(move);
			} else {
				throw new RuntimeException("Invalid move: " + from.toString() + " " + to.toString());
			}
		} else {
			throw new RuntimeException("Invalid game state");
		}
	}
	

	public GameStatus executeMove(Move move) {
		assert(boardPila.getMovimientosPosibles().contains(move));
		
		boardState.pushState();
		boardPila.push();
		
		move.execute(tablero);
		
		boardState.saveState();
		boardPila.setMovimientoSeleccionado(move);
		
		return updateGameStatus();
	}


	public GameStatus undoMove() {
		Move lastMove = boardPila.getMovimientoSeleccionado();
		lastMove.undo(tablero);
		
		boardPila.pop();
		boardState.popState();
		
		return getGameStatus();
	}
	
	protected GameStatus updateGameStatus() {
		Collection<Move> movimientosPosibles = tablero.getLegalMoves();
		GameStatus status = null;
		
		if(movimientosPosibles.isEmpty()){
			if( tablero.isKingInCheck(boardState.getTurnoActual()) ){
				status = GameStatus.JAQUE_MATE;
			} else {
				status = GameStatus.TABLAS;
			}
		} else {
			status = GameStatus.IN_PROGRESS;
		}
		
		boardPila.setMovimientosPosibles(movimientosPosibles);
		boardPila.setStatus(status);
		
		return status;
	}
	
	protected Move getMovimiento(Square from, Square to) {
		Move moveResult = null;
		for (Move move : boardPila.getMovimientosPosibles()) {
			if(from.equals(move.getFrom().getKey()) && to.equals(move.getTo().getKey())){
				moveResult = move;
			}
		}
		return moveResult;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(tablero.toString());
		buffer.append("Turno: " + getTurnoActual() + "\n");
		return buffer.toString();
	}

	public final DummyBoard getTablero() {
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
	
	protected Set<Move> createMoveContainer(){
		return new HashSet<Move>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2237718042714336104L;

			@Override
			public String toString() {
				StringBuffer buffer = new StringBuffer(); 
				for (Move move : this) {
					buffer.append(move.toString() + "\n");
				}
				return buffer.toString();
			}
		};
	}

	public BoardState getBoardState() {
		return boardState;
	}

}
