package chess;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Board {
	private DummyBoard tablero;
	
	private Color turnoActual;
	
	private Deque<Move> stackMoves = new ArrayDeque<Move>();
	
	private Set<Move> movimientosPosibles;
	
	private GameStatus status;
	
	public Board(DummyBoard tablero, Color turno){
		this.tablero = tablero;
		this.turnoActual = turno;
		updateGameStatus();
	}

	public GameStatus executeMove(Square from, Square to) {
		if(GameStatus.IN_PROGRESS.equals(this.status)){
			Move move = getMovimiento(from, to);
			if(move != null) {
				executeMove(move);
			} else {
				throw new RuntimeException("Invalid move: " + from.toString() + " " + to.toString());
			}
		} else {
			throw new RuntimeException("Invalid game state");
		}
		return this.status;
	}
	

	protected GameStatus executeMove(Move move) {
		assert(movimientosPosibles.contains(move));
		move.execute(this);
		stackMoves.push(move);
		turnoActual = turnoActual.opositeColor();
		updateGameStatus();
		return this.status;
	}

	protected void updateGameStatus() {
		movimientosPosibles = getMoves(turnoActual);
		if(movimientosPosibles.isEmpty()){
			if( tablero.isKingInCheck(turnoActual) ){
				this.status = GameStatus.JAQUE_MATE;
			} else {
				this.status = GameStatus.TABLAS;
			}
		} else {
			this.status = GameStatus.IN_PROGRESS;
		}
	}

	protected Set<Move> getMoves(Color color){
		Set<Move> moves = new HashSet<Move>();
		for (Map.Entry<Square, Pieza> entry : tablero) {
			Square currentSquare = entry.getKey();
			Pieza currentPieza = entry.getValue();
			if(currentPieza != null){
				if(color.equals(currentPieza.getColor())){
					moves.addAll(currentPieza.getLegalMoves(this, currentSquare));
				}
			}
		}
		return moves;
	}
	
	private Move getMovimiento(Square from, Square to) {
		Move moveResult = null;
		for (Move move : movimientosPosibles) {
			if(from.equals(move.getFrom()) && to.equals(move.getTo())){
				moveResult = move;
			}
		}
		return moveResult;
	}	
	
	public final DummyBoard getTablero() {
		return tablero;
	}

	public final Set<Move> getMovimientosPosibles() {
		return movimientosPosibles;
	}

	public final Color getTurnoActual() {
		return turnoActual;
	}

	public final GameStatus getGameStatus() {
		return this.status;
	}

}
