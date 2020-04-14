package chess;

import java.util.List;
import java.util.Map;

public interface Move {

	Map.Entry<Square, Pieza> getFrom();
	Map.Entry<Square, Pieza> getTo();

	void executeMove(Board board);
	void undoMove(Board board);
	
	void executeState(BoardState boardState);
	void undoState(BoardState boardState);

	void executeSquareLists(List<Square> squaresTurno, List<Square> squaresOpenente);
	void undoSquareLists(List<Square> squaresTurno, List<Square> squaresOpenente);	
}