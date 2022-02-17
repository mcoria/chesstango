package chess.moves.imp;

import chess.PiecePositioned;
import chess.legalmovesgenerators.MoveFilter;
import chess.moves.MoveKing;
import chess.position.ChessPosition;
import chess.position.imp.KingCacheBoard;

/**
 * @author Mauricio Coria
 *
 */
class SimpleKingMove extends SimpleMove implements MoveKing {

	public SimpleKingMove(PiecePositioned from, PiecePositioned to) {
		super(from, to);
	}

	@Override
	public void executeMove(ChessPosition chessPosition) {
		chessPosition.executeMove(this);
	}

	@Override
	public void undoMove(ChessPosition chessPosition) {
		chessPosition.undoMove(this);
	}

	@Override
	public boolean filter(MoveFilter filter) {
		return filter.filterMove(this);
	}

	@Override
	public void executeMove(KingCacheBoard kingCacheBoard) {
		kingCacheBoard.setKingSquare(getFrom().getValue().getColor(), getTo().getKey());
	}

	@Override
	public void undoMove(KingCacheBoard kingCacheBoard) {
		kingCacheBoard.setKingSquare(getFrom().getValue().getColor(), getFrom().getKey());
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj) && obj instanceof SimpleKingMove) {
			return true;
		}
		return false;
	}

}
