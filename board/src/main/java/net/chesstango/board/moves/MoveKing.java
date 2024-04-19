package net.chesstango.board.moves;

import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.KingSquareWriter;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveKing extends Move {

	default void executeMove(ChessPosition chessPosition){
		executeMove(chessPosition.getSquareBoard());

		executeMove(chessPosition.getBitBoard());

		executeMove(chessPosition.getPositionState());

		executeMove(chessPosition.getMoveCache());

		executeMove(chessPosition.getZobrist(), chessPosition);

		executeMove(chessPosition.getKingSquare());
	}

	default void undoMove(ChessPosition chessPosition){
		undoMove(chessPosition.getSquareBoard());

		undoMove(chessPosition.getBitBoard());

		undoMove(chessPosition.getPositionState());

		undoMove(chessPosition.getMoveCache());

		undoMove(chessPosition.getZobrist());

		undoMove(chessPosition.getKingSquare());
	}

	default boolean filter(MoveFilter filter){
		return filter.filterMoveKing(this);
	}

	default void executeMove(KingSquareWriter kingSquareWriter) {
		kingSquareWriter.setKingSquare(getFrom().getPiece().getColor(), getTo().getSquare());
	}

	default void undoMove(KingSquareWriter kingSquareWriter) {
		kingSquareWriter.setKingSquare(getFrom().getPiece().getColor(), getFrom().getSquare());
	}

}
