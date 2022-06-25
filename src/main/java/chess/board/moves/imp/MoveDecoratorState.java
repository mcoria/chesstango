package chess.board.moves.imp;

import java.util.function.Consumer;

import chess.board.movesgenerators.legal.MoveFilter;
import chess.board.moves.Move;
import chess.board.position.ChessPositionWriter;
import chess.board.position.imp.PositionState;


/**
 * @author Mauricio Coria
 *
 */
class MoveDecoratorState<T extends Move> extends MoveDecorator<T> {
	
	protected Consumer<PositionState> decoratorState;

	public MoveDecoratorState(T move, Consumer<PositionState> decoratorState) {
		super(move);
		this.decoratorState = decoratorState;
	}
	
	@Override
	public void executeMove(PositionState positionState) {
		super.executeMove(positionState);
		decoratorState.accept(positionState);
	}

}
