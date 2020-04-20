package chess;

import java.util.List;

public interface Move {

	PosicionPieza getFrom();
	PosicionPieza getTo();

	void executeMove(DummyBoard board);
	void undoMove(DummyBoard board);
	
	void executeMove(BoardState boardState);
	void undoMove(BoardState boardState);

	void executeMove(List<Square> squaresTurno, List<Square> squaresOpenente);
	void undoMove(List<Square> squaresTurno, List<Square> squaresOpenente);	
}