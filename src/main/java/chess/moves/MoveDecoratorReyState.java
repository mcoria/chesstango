package chess.moves;

import java.util.function.Consumer;

import chess.legalmovesgenerators.MoveFilter;
import chess.position.ChessPosition;
import chess.position.KingCacheBoard;
import chess.position.PositionState;

//TODO: hay que reflotar la idea del MoveKing interface, mmmm nos pasa lo mismo en este decorator

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
	public void executeMove(ChessPosition chessPosition) {
		chessPosition.executeKingMove(this);
	}
	
	@Override
	public void undoMove(ChessPosition chessPosition) {
		chessPosition.undoKingMove(this);
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
