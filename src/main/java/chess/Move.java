package chess;

import layers.ColorBoard;
import layers.DummyBoard;

//TOOD: Y si en vez de PosicionPieza utilizamos Square ?
public interface Move {

	PosicionPieza getFrom();
	PosicionPieza getTo();

	void executeMove(DummyBoard board);
	void undoMove(DummyBoard board);
	
	void executeMove(BoardState boardState);
	void undoMove(BoardState boardState);

	void executeMove(ColorBoard coloBoard);
	void undoMove(ColorBoard colorBoard);
	
	void executeMove(MoveCache moveCache);
	void undoMove(MoveCache moveCache);	
}