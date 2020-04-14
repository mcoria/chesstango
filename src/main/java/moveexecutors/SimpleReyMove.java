package moveexecutors;

import chess.Board;
import chess.PosicionPieza;

public class SimpleReyMove extends SimpleMove {

	public SimpleReyMove(PosicionPieza from, PosicionPieza to) {
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
