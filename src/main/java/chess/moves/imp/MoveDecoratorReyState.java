package chess.moves.imp;

import java.util.function.Consumer;

import chess.legalmovesgenerators.MoveFilter;
import chess.moves.MoveKing;
import chess.position.ChessPositionWriter;
import chess.position.imp.KingCacheBoard;
import chess.position.imp.PositionState;

/**
 * @author Mauricio Coria
 *
 */
class MoveDecoratorKingState extends MoveDecorator<MoveKing> implements MoveKing   {
	
	protected Consumer<PositionState> decoratorState;

	public MoveDecoratorKingState(MoveKing move, Consumer<PositionState> decoratorState) {
		super(move);
		this.decoratorState = decoratorState;
	}
	
	@Override
	public void executeMove(PositionState positionState) {
		super.executeMove(positionState);
		decoratorState.accept(positionState);
	}	
	
	@Override
	public void executeMove(ChessPositionWriter chessPosition) {
		chessPosition.executeMove(this);
	}
	
	@Override
	public void undoMove(ChessPositionWriter chessPosition) {
		chessPosition.undoMove(this);
	}	
	
	@Override
	public boolean filter(MoveFilter filter){
		return filter.filterMove(this);
	}
	
	@Override
	public void executeMove(KingCacheBoard kingCacheBoard){
		move.executeMove(kingCacheBoard);
	}
	
	@Override
	public void undoMove(KingCacheBoard kingCacheBoard){
		move.undoMove(kingCacheBoard);
	}	
}
