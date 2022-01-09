package chess.moves;

import java.util.function.Consumer;

import chess.layers.ChessPositionState;
import chess.pseudomovesfilters.MoveFilter;


/**
 * @author Mauricio Coria
 *
 */
class MoveDecoratorState extends MoveDecorator {
	
	protected Consumer<ChessPositionState> decoratorState;

	public MoveDecoratorState(Move move, Consumer<ChessPositionState> decoratorState) {
		super(move);
		this.decoratorState = decoratorState;
	}

	@Override
	public boolean filter(MoveFilter filter) {
		return filter.filterMove(this);
	}
	
	@Override
	public void executeMove(ChessPositionState chessPositionState) {
		super.executeMove(chessPositionState);
		decoratorState.accept(chessPositionState);
	}

}
