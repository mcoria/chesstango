package net.chesstango.board.moves.imp;

import java.util.function.Consumer;

import net.chesstango.board.moves.MoveKing;
import net.chesstango.board.position.imp.PositionState;

/**
 * @author Mauricio Coria
 *
 */
public class MoveDecoratorKingState extends MoveDecoratorState<MoveKing> implements MoveKing {

	public MoveDecoratorKingState(MoveKing move, Consumer<PositionState> decoratorState) {
		super(move, decoratorState);
	}
}
