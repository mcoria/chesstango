package moveexecutors;

import java.util.Map.Entry;

import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class SimpleReyNegroMove extends SimpleMove {

	public SimpleReyNegroMove(Entry<Square, Pieza> from, Entry<Square, Pieza> to) {
		super(from, to);
	}

	@Override
	public void executeMove(DummyBoard board) {
		super.executeMove(board);
		board.setSquareKingNegroCache(to.getKey());
	}
	
	
	@Override
	public void undoMove(DummyBoard board) {
		super.undoMove(board);
		board.setSquareKingNegroCache(from.getKey());
	}	
}
