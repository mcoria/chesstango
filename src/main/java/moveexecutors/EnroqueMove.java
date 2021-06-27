package moveexecutors;

import chess.Board;
import chess.PosicionPieza;
import layers.ColorBoard;
import layers.PosicionPiezaBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;

public abstract class EnroqueMove extends AbstractMove {
	
	protected abstract SimpleReyMove getReyMove();
	protected abstract SimpleMove getTorreMove();
	
	public EnroqueMove(PosicionPieza from, PosicionPieza to) {
		super(from, to);
	}
	
	
	@Override
	public void executePseudo(Board board){
		super.executePseudo(board);
		executeMove(board.getKingCacheBoard());	
	}
	
	@Override
	public void undoPseudo(Board board){
		super.undoPseudo(board);
		undoMove(board.getKingCacheBoard());	
	}
	
	@Override
	public void execute(Board board) {
		super.execute(board);
		executeMove(board.getKingCacheBoard());
	}
	
	@Override
	public void undo(Board board) {
		super.undo(board);
		undoMove(board.getKingCacheBoard());
	}
	
	@Override
	public void executeMove(PosicionPiezaBoard board) {
		getReyMove().executeMove(board);
		getTorreMove().executeMove(board);
	}


	@Override
	public void undoMove(PosicionPiezaBoard board) {
		getReyMove().undoMove(board);
		getTorreMove().undoMove(board);
	}	
	
	@Override
	public void executeMove(ColorBoard colorBoard) {
		getReyMove().executeMove(colorBoard);
		getTorreMove().executeMove(colorBoard);
	}

	@Override
	public void undoMove(ColorBoard colorBoard) {
		getReyMove().undoMove(colorBoard);
		getTorreMove().undoMove(colorBoard);
	}	
	
	
	@Override
	public void executeMove(MoveCacheBoard moveCache) {
		moveCache.pushState();
		moveCache.clearPseudoMoves(getReyMove().getFrom().getKey(), getReyMove().getTo().getKey(), getTorreMove().getFrom().getKey(), getTorreMove().getTo().getKey());
	}
	
	public void executeMove(KingCacheBoard kingCacheBoard){
		getReyMove().executeMove(kingCacheBoard);
	}

	public void undoMove(KingCacheBoard kingCacheBoard){
		getReyMove().undoMove(kingCacheBoard);	
	}	

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof EnroqueMove){
			EnroqueMove theOther = (EnroqueMove) obj;
			return getFrom().equals(theOther.getFrom()) &&  getTo().equals(theOther.getTo());
		}
		return false;
	}

}
