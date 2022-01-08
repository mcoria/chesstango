package chess.moves;

import java.util.function.Consumer;

import chess.Board;
import chess.BoardState;
import chess.pseudomovesfilters.MoveFilter;

//TODO: hay que reflotar la idea del MoveKing interface, mmmm nos pasa lo mismo en este decorator

/**
 * @author Mauricio Coria
 *
 */
class MoveDecoratorKingState extends MoveDecoratorState {

	public MoveDecoratorKingState(Move move, Consumer<BoardState> decoratorState) {
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
		return filter.filterMoveKing(this);
	}
}
