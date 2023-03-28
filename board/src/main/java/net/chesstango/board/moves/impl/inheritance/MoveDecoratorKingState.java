package net.chesstango.board.moves.impl.inheritance;

import net.chesstango.board.moves.MoveKing;
import net.chesstango.board.position.imp.PositionState;

import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 *
 */
class MoveDecoratorKingState extends MoveDecoratorState<MoveKing> implements MoveKing {

	public MoveDecoratorKingState(MoveKing move, Consumer<PositionState> decoratorState) {
		super(move, decoratorState);
	}
}
