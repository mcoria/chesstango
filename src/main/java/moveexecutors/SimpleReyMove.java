package moveexecutors;

import chess.PosicionPieza;
import layers.KingCacheBoard;

public class SimpleReyMove extends SimpleMove{

	
	public SimpleReyMove(PosicionPieza from, PosicionPieza to) {
		super(from, to);
	}
	
	@Override
	public void executeMove(KingCacheBoard kingCacheBoard){
		kingCacheBoard.setKingSquare(from.getValue().getColor(), to.getKey());
	}
	
	@Override
	public void undoMove(KingCacheBoard kingCacheBoard){
		kingCacheBoard.setKingSquare(from.getValue().getColor(), from.getKey());	
	}	
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof SimpleReyMove){
			return true;
		}
		return false;
	}
	
	@Override
	protected String getType() {
		return "SimpleReyMove";
	}	

}
