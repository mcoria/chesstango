package moveexecutors;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;
import java.util.Map.Entry;

import chess.BoardState;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class EnroqueBlancoReyMove extends AbstractMove {

	public static final Map.Entry<Square, Pieza> FROM = new SimpleImmutableEntry<Square, Pieza>(Square.e1, Pieza.REY_BLANCO);
	
	public static final Map.Entry<Square, Pieza> TO = new SimpleImmutableEntry<Square, Pieza>(Square.g1, null);
	
	public EnroqueBlancoReyMove() {
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
		return "EnroqueBlancoReyMove";
	}
	
	public void execute(DummyBoard board, Map.Entry<Square, Pieza> from, Map.Entry<Square, Pieza> to) {
		board.setEmptySquare(Square.e1);
		board.setEmptySquare(Square.h1);
		board.setPieza(Square.g1, Pieza.REY_BLANCO);
		board.setPieza(Square.f1, Pieza.TORRE_BLANCO);
		
		BoardState boardState = board.getBoardState();	
		boardState.setEnroqueBlancoReyPermitido(false);
		boardState.setEnroqueBlancoReinaPermitido(false);
		boardState.setPeonPasanteSquare(null);	
		boardState.rollTurno();
	}

	public void undo(DummyBoard board, Entry<Square, Pieza> from, Entry<Square, Pieza> to) {
		board.setEmptySquare(Square.g1);
		board.setEmptySquare(Square.f1);
		board.setPieza(Square.e1, Pieza.REY_BLANCO);
		board.setPieza(Square.h1, Pieza.TORRE_BLANCO);
		
		BoardState boardState = board.getBoardState();		
		boardState.restoreState();		
	}

}
