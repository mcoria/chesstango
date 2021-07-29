package moveexecutors;

import chess.BoardState;
import chess.Move;
import chess.Pieza;
import layers.KingCacheBoard;
import movecalculators.MoveFilter;

public abstract class AbstractKingMove extends MoveDecorator {
	
	public AbstractKingMove(Move move) {
		super(move);
	}

	//TODO: Esto deberia ser un decorator
	@Override
	public void executeMove(BoardState boardState) {
		move.executeMove(boardState);
		if(Pieza.REY_BLANCO.equals(move.getFrom().getValue()) ){
			boardState.setEnroqueBlancoReinaPermitido(false);
			boardState.setEnroqueBlancoReyPermitido(false);
		} else {
			boardState.setEnroqueNegroReinaPermitido(false);
			boardState.setEnroqueNegroReyPermitido(false);
		}
	}
	
	@Override
	public void executeMove(KingCacheBoard kingCacheBoard){
		kingCacheBoard.setKingSquare(getFrom().getValue().getColor(), getTo().getKey());
	}
	
	@Override
	public void undoMove(KingCacheBoard kingCacheBoard){
		kingCacheBoard.setKingSquare(getFrom().getValue().getColor(), getFrom().getKey());	
	}
	
	@Override
	public boolean filter(MoveFilter filter){
		return filter.filterKingMove(this);
	}
	
}
