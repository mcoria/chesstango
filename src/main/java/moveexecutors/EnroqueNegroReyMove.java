package moveexecutors;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.List;
import java.util.Map;

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
	public void executeMove(DummyBoard board) {
		board.setEmptySquare(Square.e8);
		board.setEmptySquare(Square.h8);
		board.setPieza(Square.g8, Pieza.REY_NEGRO);
		board.setPieza(Square.f8, Pieza.TORRE_NEGRO);
	}

	@Override
	public void undoMove(DummyBoard board) {
		board.setEmptySquare(Square.g8);
		board.setEmptySquare(Square.f8);
		board.setPieza(Square.e8, Pieza.REY_NEGRO);
		board.setPieza(Square.h8, Pieza.TORRE_NEGRO);	
	}
	
	@Override
	public void executeState(BoardState boardState) {
		boardState.pushState();
		boardState.setEnroqueNegroReyPermitido(false);
		boardState.setEnroqueNegroReinaPermitido(false);
		boardState.setPeonPasanteSquare(null);		
		boardState.rollTurno();
	}
	
	@Override
	public void executeSquareLists(List<Square> squaresTurno, List<Square> squaresOpenente) {
		squaresTurno.remove(Square.e8);
		squaresTurno.remove(Square.h8);
		squaresTurno.add(Square.g8);
		squaresTurno.add(Square.f8);
	}

	@Override
	public void undoSquareLists(List<Square> squaresTurno, List<Square> squaresOpenente) {
		squaresTurno.add(Square.e8);
		squaresTurno.add(Square.h8);
		squaresTurno.remove(Square.g8);
		squaresTurno.remove(Square.f8);
	}	
	
	@Override
	protected String getType() {
		return "EnroqueNegroReyMove";
	}	

}
