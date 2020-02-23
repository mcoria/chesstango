package moveexecutors;

import java.util.Map.Entry;

import chess.BoardState;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class SaltoDoblePeonMove extends AbstractMove {
	
	private final Square peonPasanteSquare;

	public SaltoDoblePeonMove(Entry<Square, Pieza> from, Entry<Square, Pieza> to, Square peonPasanteSquare) {
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
		boardState.setPeonPasanteSquare(peonPasanteSquare);
		boardState.rollTurno();
	}

	@Override
	public void undoState(BoardState boardState) {
	}	
	
	@Override
	protected String getType() {
		return "SaltoDoblePeonMove";
	}
}
