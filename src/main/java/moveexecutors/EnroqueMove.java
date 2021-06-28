package moveexecutors;

import chess.Move;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;

public abstract class EnroqueMove extends AbstractKingMove  {
	
	protected abstract SimpleReyMove getReyMove();
	protected abstract SimpleMove getTorreMove();	
	
	public EnroqueMove(Move move) {
		super(move);
		// TODO Auto-generated constructor stub
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
		SimpleReyMove reyMove = getReyMove();
		SimpleMove torreMove = getTorreMove();
		moveCache.clearPseudoMoves(reyMove.getFrom().getKey(), reyMove.getTo().getKey(), torreMove.getFrom().getKey(), torreMove.getTo().getKey());
	}
	
	@Override
	public void executeMove(KingCacheBoard kingCacheBoard) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void undoMove(KingCacheBoard kingCacheBoard) {
		// TODO Auto-generated method stub
		
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
	public int compareTo(Move o) {
		// TODO Auto-generated method stub
		return 0;
	}	

}
