package moveexecutors;

import java.util.Map;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;

import chess.BoardState;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class EnroqueNegroReyMove extends AbstractMove {	

	public static final Map.Entry<Square, Pieza> FROM = new SimpleImmutableEntry<Square, Pieza>(Square.e8, Pieza.REY_NEGRO);
	
	public static final Map.Entry<Square, Pieza> TO = new SimpleImmutableEntry<Square, Pieza>(Square.g8, null);
	
	public EnroqueNegroReyMove() {
		super(FROM, TO);
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
		return "EnroqueNegroReyMove";
	}
	

	public void execute(DummyBoard board, Map.Entry<Square, Pieza> from, Map.Entry<Square, Pieza> to) {
		board.setEmptySquare(Square.e8);
		board.setEmptySquare(Square.h8);
		board.setPieza(Square.g8, Pieza.REY_NEGRO);
		board.setPieza(Square.f8, Pieza.TORRE_NEGRO);
		
		BoardState boardState = board.getBoardState();
		boardState.setEnroqueNegroReyPermitido(false);
		boardState.setEnroqueNegroReinaPermitido(false);
		boardState.setPeonPasanteSquare(null);		
		boardState.rollTurno();
	}

	public void undo(DummyBoard board, Entry<Square, Pieza> from, Entry<Square, Pieza> to) {
		board.setEmptySquare(Square.g8);
		board.setEmptySquare(Square.f8);
		board.setPieza(Square.e8, Pieza.REY_NEGRO);
		board.setPieza(Square.h8, Pieza.TORRE_NEGRO);
	}

}
