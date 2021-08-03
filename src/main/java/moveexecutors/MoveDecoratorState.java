package moveexecutors;

import java.util.function.Consumer;

import chess.Board;
import chess.BoardState;
import movecalculators.MoveFilter;

public class MoveDecoratorState extends MoveDecorator {
	
	protected Consumer<BoardState> decoratorState;

	public MoveDecoratorState(Move move, Consumer<BoardState> decoratorState) {
		super(move);
		this.decoratorState = decoratorState;
	}
	
	@Override
	public void executeMove(Board board) {
		board.executeMove(this);
	}

	@Override
	public void undoMove(Board board) {
		board.undoMove(this);
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
