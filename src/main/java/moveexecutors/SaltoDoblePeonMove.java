package moveexecutors;

import java.util.List;

import chess.BoardState;
import chess.DummyBoard;
import chess.PosicionPieza;
import chess.Square;

public class SaltoDoblePeonMove extends AbstractMove {
	
	private final Square peonPasanteSquare;

	public SaltoDoblePeonMove(PosicionPieza from, PosicionPieza to, Square peonPasanteSquare) {
		super(from, to);
		this.peonPasanteSquare = peonPasanteSquare;
	}
	
	@Override
	public void executeMove(DummyBoard board) {
		board.setEmptySquare(from.getKey());								//Dejamos origen
		board.setPieza(to.getKey(), from.getValue());						//Vamos a destino
	}

	@Override
	public void undoMove(DummyBoard board) {
		board.setPosicion(to);							//Reestablecemos destino
		board.setPosicion(from);						//Volvemos a origen		
	}
	
	@Override
	public void executeState(BoardState boardState) {
		boardState.pushState();
		boardState.setPeonPasanteSquare(peonPasanteSquare);
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
		if(super.equals(obj)  && obj instanceof SaltoDoblePeonMove){
			return true;
		}
		return false;
	}
	
	@Override
	protected String getType() {
		return "SaltoDoblePeonMove";
	}
}
