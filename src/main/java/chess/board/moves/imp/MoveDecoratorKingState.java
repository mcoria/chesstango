package chess.board.moves.imp;

import java.util.function.Consumer;

import chess.board.movesgenerators.legal.MoveFilter;
import chess.board.moves.MoveKing;
import chess.board.position.ChessPositionWriter;
import chess.board.position.imp.KingCacheBoard;
import chess.board.position.imp.PositionState;

/**
 * @author Mauricio Coria
 *
 */
class MoveDecoratorKingState extends MoveDecoratorState<MoveKing> implements MoveKing {

	public MoveDecoratorKingState(MoveKing move, Consumer<PositionState> decoratorState) {
		super(move, decoratorState);
	}
}
