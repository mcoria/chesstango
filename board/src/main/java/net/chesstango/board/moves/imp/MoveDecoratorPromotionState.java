package net.chesstango.board.moves.imp;

import net.chesstango.board.Piece;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.position.imp.PositionState;

import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 *
 */
public class MoveDecoratorPromotionState extends MoveDecoratorState<MovePromotion> implements MovePromotion {

	public MoveDecoratorPromotionState(MovePromotion move, Consumer<PositionState> decoratorState) {
		super(move, decoratorState);
	}

	@Override
	public Piece getPromotion() {
		return move.getPromotion();
	}
}
