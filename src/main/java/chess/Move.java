package chess;

import java.util.Map;

public interface Move {

	Map.Entry<Square, Pieza> getFrom();

	Map.Entry<Square, Pieza> getTo();

	void execute(DummyBoard board);
	void undo(DummyBoard board);
	
	void executeMove(DummyBoard board);
	void undoMove(DummyBoard board);

}