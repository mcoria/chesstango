package chess;

import java.util.Map;

public interface Move {

	Map.Entry<Square, Pieza> getFrom();
	Map.Entry<Square, Pieza> getTo();

	void executeMove(DummyBoard board);
	void undoMove(DummyBoard board);
	
	void executeState(BoardState boardState);
	void undoState(BoardState boardState);


}