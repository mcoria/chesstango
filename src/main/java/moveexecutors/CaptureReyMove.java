package moveexecutors;

import chess.Board;
import chess.PosicionPieza;
import layers.KingCacheBoard;

public class CaptureReyMove extends CaptureMove {

	
	public CaptureReyMove(PosicionPieza from, PosicionPieza to) {
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
	
	public void executeMove(KingCacheBoard kingCacheBoard){
		kingCacheBoard.setKingSquare(from.getValue().getColor(), to.getKey());
	}

	public void undoMove(KingCacheBoard kingCacheBoard){
		kingCacheBoard.setKingSquare(from.getValue().getColor(), from.getKey());	
	}	
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof CaptureReyMove){
			return true;
		}
		return false;
	}
	
	@Override
	protected String getType() {
		return "CaptureReyMove";
	}	
}
