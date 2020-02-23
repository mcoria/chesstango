package moveexecutors;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;

import chess.BoardState;
import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class EnroqueNegroReynaMove extends AbstractMove {

	public static final Map.Entry<Square, Pieza> FROM = new SimpleImmutableEntry<Square, Pieza>(Square.e8, Pieza.REY_NEGRO);
	
	public static final Map.Entry<Square, Pieza> TO = new SimpleImmutableEntry<Square, Pieza>(Square.c8, null);
	
	public EnroqueNegroReynaMove() {
		super(FROM, TO);
	}

	@Override
	public void execute(DummyBoard board) {
		this.executeMove(board);
		BoardState boardState = board.getBoardState();	
		boardState.setEnroqueNegroReyPermitido(false);
		boardState.setEnroqueNegroReinaPermitido(false);
		boardState.setPeonPasanteSquare(null);		
		boardState.rollTurno();		
		boardState.saveState();
	}

	@Override
	public void undo(DummyBoard board) {
		this.undoMove(board);
		BoardState boardState = board.getBoardState();		
		boardState.restoreState();		
	}
	

	@Override
	protected String getType() {
		return "EnroqueNegroReynaMove";
	}
	
	@Override
	public void executeMove(DummyBoard board) {
		board.setEmptySquare(Square.e8);
		board.setEmptySquare(Square.a8);
		board.setPieza(Square.c8, Pieza.REY_NEGRO);
		board.setPieza(Square.d8, Pieza.TORRE_NEGRO);
	}

	@Override
	public void undoMove(DummyBoard board) {
		board.setEmptySquare(Square.c8);
		board.setEmptySquare(Square.d8);
		board.setPieza(Square.e8, Pieza.REY_NEGRO);
		board.setPieza(Square.a8, Pieza.TORRE_NEGRO);		
	}

}
