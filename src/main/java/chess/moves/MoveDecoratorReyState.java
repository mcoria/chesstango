package chess.moves;

import java.util.function.Consumer;

import chess.position.ChessPosition;
import chess.position.PositionState;
import chess.pseudomovesfilters.MoveFilter;

//TODO: hay que reflotar la idea del MoveKing interface, mmmm nos pasa lo mismo en este decorator

/**
 * @author Mauricio Coria
 *
 */
class MoveDecoratorKingState extends MoveDecoratorState {

	public MoveDecoratorKingState(Move move, Consumer<PositionState> decoratorState) {
		super(move, decoratorState);
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
		return filter.filterKingMove(this);
	}
}
