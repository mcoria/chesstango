package moveexecutors;

import java.util.Map.Entry;

import chess.Board;
import chess.Pieza;
import chess.Square;

public class CaptureReyMove extends CaptureMove {

	public CaptureReyMove(Entry<Square, Pieza> from, Entry<Square, Pieza> to) {
		super(from, to);
	}
	
	@Override
	public void executeMove(Board board) {
		super.executeMove(board);
		SquareKingCacheSetter kingCacheSetter = board.getSquareKingCacheSetter(from.getValue().getColor());
		kingCacheSetter.setSquareKingCache(to.getKey());
	}
	
	
	@Override
	public void undoMove(Board board) {
		super.undoMove(board);
		SquareKingCacheSetter kingCacheSetter = board.getSquareKingCacheSetter(from.getValue().getColor());
		kingCacheSetter.setSquareKingCache(from.getKey());
	}

}
