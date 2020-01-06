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

	public GameStatus move(Move move) {
		if(this.status.equals(GameStatus.IN_PROGRESS)){
			if ( movimientosPosibles.contains(move) ) {
				tablero.move(move);
				stackMoves.push(move);
				turnoActual = turnoActual.opositeColor();
				updateGameStatus();
			} else {
				throw new RuntimeException("Invalid move");
			}
		} else {
			throw new RuntimeException("Invalid game state");
		}
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
	
	public final DummyBoard getTablero() {
		return tablero;
	}
	
	private final Set<Move> getMovimientosPosibles() {
		return movimientosPosibles;
	}	
	


}
