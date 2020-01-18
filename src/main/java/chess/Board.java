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
	
	private BoardState boardState;
	
	public Board(DummyBoard tablero, Color turno, BoardState boardState){
		this.tablero = tablero;
		this.turnoActual = turno;
		this.boardState = boardState;
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
	

	public GameStatus executeMove(Move move) {
		assert(movimientosPosibles.contains(move));
		move.execute(tablero, boardState);
		stackMoves.push(move);
		turnoActual = turnoActual.opositeColor();
		updateGameStatus();
		return this.status;
	}


	public GameStatus undoMove() {
		Move lastMove = stackMoves.pop();
		lastMove.undo(tablero, boardState);
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
		Set<Move> moves = createMoveContainer();
		for (Map.Entry<Square, Pieza> origen : tablero) {
			Pieza currentPieza = origen.getValue();
			if(currentPieza != null){
				if(color.equals(currentPieza.getColor())){
					moves.addAll(currentPieza.getLegalMoves(tablero, boardState, origen));
				}
			}
		}
		return moves;
	}
	
	private Move getMovimiento(Square from, Square to) {
		Move moveResult = null;
		for (Move move : movimientosPosibles) {
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
		buffer.append("Turno: " + this.turnoActual + "\n");
		return buffer.toString();
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
