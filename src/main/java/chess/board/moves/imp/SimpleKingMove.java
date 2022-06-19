package chess.board.moves.imp;

import chess.board.PiecePositioned;
import chess.board.movesgenerators.legal.MoveFilter;
import chess.board.moves.MoveKing;
import chess.board.position.ChessPositionWriter;
import chess.board.position.imp.KingCacheBoard;

/**
 * @author Mauricio Coria
 *
 */
class SimpleKingMove extends SimpleMove implements MoveKing {

	public SimpleKingMove(PiecePositioned from, PiecePositioned to) {
		super(from, to);
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
        return super.equals(obj) && obj instanceof SimpleKingMove;
    }

}
