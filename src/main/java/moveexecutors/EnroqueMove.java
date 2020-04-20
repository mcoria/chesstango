package moveexecutors;

import java.util.List;

import chess.BoardCache;
import chess.BoardState;
import chess.DummyBoard;
import chess.Move;
import chess.PosicionPieza;
import chess.Square;
import chess.MoveKing;

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
	public void executeState(BoardState boardState) {
		super.executeState(boardState);
		boardState.setPeonPasanteSquare(null);	
	}
	
	@Override
	public void undoState(BoardState boardState) {
		boardState.popState();		
	}	
	
	@Override
	public void executeSquareLists(List<Square> squaresTurno, List<Square> squaresOpenente) {
		getReyMove().executeSquareLists(squaresTurno, squaresOpenente);
		getTorreMove().executeSquareLists(squaresTurno, squaresOpenente);
	}

	@Override
	public void undoSquareLists(List<Square> squaresTurno, List<Square> squaresOpenente) {
		getReyMove().undoSquareLists(squaresTurno, squaresOpenente);
		getTorreMove().undoSquareLists(squaresTurno, squaresOpenente);
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
