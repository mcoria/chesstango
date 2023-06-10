package net.chesstango.board.moves;

import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.ChessPositionWriter;
import net.chesstango.board.position.KingSquareWriter;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveKing extends Move {

	default void executeMove(ChessPositionWriter chessPosition){
		chessPosition.executeMoveKing(this);
	}

	default void undoMove(ChessPositionWriter chessPosition){
		chessPosition.undoMoveKing(this);
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
