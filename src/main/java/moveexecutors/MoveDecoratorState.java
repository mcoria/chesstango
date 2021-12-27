package moveexecutors;

import java.util.function.Consumer;

import chess.BoardState;
import movecalculators.MoveFilter;

class MoveDecoratorState extends MoveDecorator {
	
	protected Consumer<BoardState> decoratorState;

	public MoveDecoratorState(Move move, Consumer<BoardState> decoratorState) {
		super(move);
		this.decoratorState = decoratorState;
	}

	@Override
	public boolean filter(MoveFilter filter) {
		return filter.filterMove(this);
	}
	
	@Override
	public void executeMove(BoardState boardState) {
		super.executeMove(boardState);
		decoratorState.accept(boardState);
	}

}
