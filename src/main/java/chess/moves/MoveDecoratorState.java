package chess.moves;

import java.util.function.Consumer;

import chess.position.PositionState;
import chess.pseudomovesfilters.MoveFilter;


/**
 * @author Mauricio Coria
 *
 */
class MoveDecoratorState extends MoveDecorator {
	
	protected Consumer<PositionState> decoratorState;

	public MoveDecoratorState(Move move, Consumer<PositionState> decoratorState) {
		super(move);
		this.decoratorState = decoratorState;
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
