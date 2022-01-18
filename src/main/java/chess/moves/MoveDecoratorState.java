package chess.moves;

import java.util.function.Consumer;

import chess.legalmovesgenerators.MoveFilter;
import chess.position.ChessPosition;
import chess.position.PositionState;


/**
 * @author Mauricio Coria
 *
 */
class MoveDecoratorState extends MoveDecorator<Move> {
	
	protected Consumer<PositionState> decoratorState;

	public MoveDecoratorState(Move move, Consumer<PositionState> decoratorState) {
		super(move);
		this.decoratorState = decoratorState;
	}
	
	@Override
	public void executeMove(ChessPosition chessPosition) {
		chessPosition.executeMove(this);
	}

	@Override
	public void undoMove(ChessPosition chessPosition) {
		chessPosition.undoMove(this);
	}	

	@Override
	public boolean filter(MoveFilter filter) {
		return filter.filterMove(this);
	}
	
	@Override
	public void executeMove(PositionState positionState) {
		super.executeMove(positionState);
		decoratorState.accept(positionState);
	}

}
