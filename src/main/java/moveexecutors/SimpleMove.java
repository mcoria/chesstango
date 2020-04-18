package moveexecutors;

import java.util.List;

import chess.BoardState;
import chess.DummyBoard;
import chess.PosicionPieza;
import chess.Square;

public class SimpleMove extends AbstractMove {
	
	public SimpleMove(PosicionPieza from, PosicionPieza to) {
		super(from, to);
	}
	
	@Override
	public void executeMove(DummyBoard board) {
		board.setEmptySquare(from.getKey());					//Dejamos el origen
		board.setPieza(to.getKey(), from.getValue()) ;			//Vamos al destino	
	}

	@Override
	public void undoMove(DummyBoard board) {
		board.setPosicion(to);							//Reestablecemos destino
		board.setPosicion(from);						//Volvemos a origen
	}
	
	@Override
	public void executeState(BoardState boardState) {
		boardState.pushState();
		boardState.setPeonPasanteSquare(null);
		boardState.rollTurno();
	}
	
	@Override
	public void executeSquareLists(List<Square> squaresTurno, List<Square> squaresOpenente) {
		squaresTurno.remove(from.getKey());
		squaresTurno.add(to.getKey());
	}

	@Override
	public void undoSquareLists(List<Square> squaresTurno, List<Square> squaresOpenente) {
		squaresTurno.remove(to.getKey());
		squaresTurno.add(from.getKey());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof SimpleMove){
			return true;
		}
		return false;
	}
	
	@Override
	protected String getType() {
		return "SimpleMove";
	}
}
