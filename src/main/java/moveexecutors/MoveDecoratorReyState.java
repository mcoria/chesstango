package moveexecutors;

import java.util.function.Consumer;

import chess.Board;
import chess.BoardState;
import movecalculators.MoveFilter;

public class MoveDecoratorReyState extends MoveDecoratorState {

	public MoveDecoratorReyState(Move move, Consumer<BoardState> decoratorState) {
		super(move, decoratorState);
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
}
