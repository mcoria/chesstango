package moveexecutors;

import chess.BoardCache;
import chess.BoardState;
import chess.DummyBoard;
import chess.Move;
import chess.MoveKing;
import chess.PosicionPieza;

public abstract class EnroqueMove extends AbstractMove implements MoveKing {
	
	protected abstract SimpleReyMove getReyMove();
	protected abstract Move getTorreMove();
	
	public EnroqueMove(PosicionPieza from, PosicionPieza to) {
		super(from, to);
	}
	
	@Override
	public void executeMove(DummyBoard board) {
		getReyMove().executeMove(board);
		getTorreMove().executeMove(board);
	}


	@Override
	public void undoMove(DummyBoard board) {
		getReyMove().undoMove(board);
		getTorreMove().undoMove(board);
	}	
	
	@Override
	public void executeMove(BoardState boardState) {
		getReyMove().executeMove(boardState);
	}
	
	@Override
	public void undoMove(BoardState boardState) {
		getReyMove().undoMove(boardState);
	}	
	
	@Override
	public void executeMove(BoardCache boardCache) {
		getReyMove().executeMove(boardCache);
		getTorreMove().executeMove(boardCache);
	}

	@Override
	public void undoMove(BoardCache boardCache) {
		getReyMove().undoMove(boardCache);
		getTorreMove().undoMove(boardCache);
	}
	
	@Override
	public void executetSquareKingCache(BoardCache cache) {
		getReyMove().executetSquareKingCache(cache);
	}

	@Override
	public void undoSquareKingCache(BoardCache cache) {
		getReyMove().undoSquareKingCache(cache);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof EnroqueMove){
			EnroqueMove theOther = (EnroqueMove) obj;
			return getFrom().equals(theOther.getFrom()) &&  getTo().equals(theOther.getTo());
		}
		return false;
	}

	@Override
	public String toString() {
		return getFrom().toString() + " " + getTo().toString() + "; " + getType();
	}

	protected abstract String getType();
}
