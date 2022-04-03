package chess.board.moves.imp;

import java.util.function.Consumer;

import chess.board.legalmovesgenerators.MoveFilter;
import chess.board.moves.Move;
import chess.board.position.ChessPositionWriter;
import chess.board.position.imp.PositionState;


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
	public void executeMove(ChessPositionWriter chessPosition) {
		chessPosition.executeMove(this);
	}

	@Override
	public void undoMove(ChessPositionWriter chessPosition) {
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
