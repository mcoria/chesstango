package moveexecutors;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;

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
		return "EnroqueBlancoReyMove";
	}
	
	@Override
	public void executeMove(DummyBoard board) {
		board.setEmptySquare(Square.e1);
		board.setEmptySquare(Square.h1);
		board.setPieza(Square.g1, Pieza.REY_BLANCO);
		board.setPieza(Square.f1, Pieza.TORRE_BLANCO);
	}

	@Override
	public void undoMove(DummyBoard board) {
		board.setEmptySquare(Square.g1);
		board.setEmptySquare(Square.f1);
		board.setPieza(Square.e1, Pieza.REY_BLANCO);
		board.setPieza(Square.h1, Pieza.TORRE_BLANCO);	
	}

}
