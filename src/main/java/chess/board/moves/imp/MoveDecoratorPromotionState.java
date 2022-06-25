package chess.board.moves.imp;

import chess.board.Piece;
import chess.board.moves.MoveKing;
import chess.board.moves.MovePromotion;
import chess.board.position.imp.PositionState;

import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 *
 */
class MoveDecoratorPromotionState extends MoveDecoratorState<MovePromotion> implements MovePromotion {

	public MoveDecoratorPromotionState(MovePromotion move, Consumer<PositionState> decoratorState) {
		super(move, decoratorState);
	}

	@Override
	public Piece getPromotion() {
		return move.getPromotion();
	}
}
