package chess;

import java.util.List;

public interface Move {

	PosicionPieza getFrom();
	PosicionPieza getTo();

	void executeMove(Board board);
	void undoMove(Board board);
	
	void executeState(BoardState boardState);
	void undoState(BoardState boardState);

	void executeSquareLists(List<Square> squaresTurno, List<Square> squaresOpenente);
	void undoSquareLists(List<Square> squaresTurno, List<Square> squaresOpenente);	
}