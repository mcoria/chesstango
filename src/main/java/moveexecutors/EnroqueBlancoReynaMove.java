package moveexecutors;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;

import chess.BoardState;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class EnroqueBlancoReynaMove extends AbstractMove {
	
	public static final Map.Entry<Square, Pieza> FROM = new SimpleImmutableEntry<Square, Pieza>(Square.e1, Pieza.REY_BLANCO);
	
	public static final Map.Entry<Square, Pieza> TO = new SimpleImmutableEntry<Square, Pieza>(Square.c1, null);
	

	public EnroqueBlancoReynaMove() {
		super(FROM, TO);
	}

	@Override
	public void execute(DummyBoard board) {
		this.executeMove(board);
		BoardState boardState = board.getBoardState();		
		boardState.setEnroqueBlancoReyPermitido(false);
		boardState.setEnroqueBlancoReinaPermitido(false);
		boardState.setPeonPasanteSquare(null);	
		boardState.rollTurno();
	}

	@Override
	public void undo(DummyBoard board) {
		this.undoMove(board);	
	}

	@Override
	protected String getType() {
		return "EnroqueBlancoReynaMove";
	}
	
	@Override
	public void executeMove(DummyBoard board) {
		board.setEmptySquare(Square.e1);
		board.setEmptySquare(Square.a1);
		board.setPieza(Square.c1, Pieza.REY_BLANCO);
		board.setPieza(Square.d1, Pieza.TORRE_BLANCO);
	}

	@Override
	public void undoMove(DummyBoard board) {
		board.setEmptySquare(Square.c1);
		board.setEmptySquare(Square.d1);
		board.setPieza(Square.e1, Pieza.REY_BLANCO);
		board.setPieza(Square.a1, Pieza.TORRE_BLANCO);		
	}

}
