package moveexecutors;

import java.util.Map;
import java.util.Map.Entry;

import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class SaltoDoblePeonMove extends AbstractMove {

	public SaltoDoblePeonMove(Entry<Square, Pieza> from, Entry<Square, Pieza> to) {
		super(from, to);
	}

	@Override
	public void execute(DummyBoard board) {
		this.execute(board, from, to);
	}

	@Override
	public void undo(DummyBoard board) {
		this.undo(board, from, to);
	}

	@Override
	protected String getType() {
		return "SaltoDoblePeonMove";
	}
	
	public void execute(DummyBoard board, Map.Entry<Square, Pieza> from, Map.Entry<Square, Pieza> to) {
		Square peonPasanteSquare = Square.getSquare(to.getKey().getFile(),  Color.BLANCO.equals(from.getValue().getColor()) ? to.getKey().getRank() - 1 : to.getKey().getRank() + 1);
		board.setEmptySquare(from.getKey());								//Dejamos origen
		board.setPieza(to.getKey(), from.getValue());						//Vamos a destino
		
		BoardState boardState = board.getBoardState();
		boardState.setPeonPasanteSquare(peonPasanteSquare);
		boardState.rollTurno();
	}

	public void undo(DummyBoard board, Entry<Square, Pieza> from, Entry<Square, Pieza> to) {
		board.setPosicion(to);							//Reestablecemos destino
		board.setPosicion(from);						//Volvemos a origen
	}

}
