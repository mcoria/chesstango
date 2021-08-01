package moveexecutors;

import chess.Board;
import chess.BoardState;
import chess.Pieza;
import chess.PosicionPieza;
import layers.ColorBoard;
import layers.KingCacheBoard;
import movecalculators.MoveFilter;

public class SimpleReyMove extends AbstractMove {

	
	public SimpleReyMove(PosicionPieza from, PosicionPieza to) {
		super(from, to);
	}
	
	@Override
	public void executeMove(Board board) {
		board.executeKingMove(this);
	}
	
	@Override
	public void undoMove(Board board) {
		board.undoKingMove(this);
	}	
	
	@Override
	public boolean filter(MoveFilter filter){
		return filter.filterKingMove(this);
	}
	
	@Override
	public void executeMove(ColorBoard colorBoard) {
		colorBoard.swapPositions(from.getValue().getColor(), from.getKey(), to.getKey());
	}
	
	@Override
	public void undoMove(ColorBoard colorBoard) {
		colorBoard.swapPositions(from.getValue().getColor(), to.getKey(), from.getKey());
	}
	
	//TODO: Esto deberia ser un decorator
	@Override
	public void executeMove(BoardState boardState) {
		super.executeMove(boardState);
		if(Pieza.REY_BLANCO.equals(this.getFrom().getValue()) ){
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
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof SimpleReyMove){
			return true;
		}
		return false;
	}

}
