package net.chesstango.board.moves;

import net.chesstango.board.movesgenerators.legal.MoveFilter;
import net.chesstango.board.position.*;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveKing extends Move {

	@Override
	default void executeMove(SquareBoardWriter squareBoard,
							 BitBoardWriter bitBoard,
							 PositionStateWriter positionState,
							 MoveCacheBoardWriter moveCache,
							 KingSquareWriter kingSquare, ZobristHashWriter hash,
							 ChessPositionReader chessPositionReader) {
		executeMove(squareBoard);

		executeMove(bitBoard);

		executeMove(positionState);

		executeMove(moveCache);

		executeMove(kingSquare);

		executeMove(hash, chessPositionReader);
	}

	@Override
	default void undoMove(SquareBoardWriter squareBoard,
						  BitBoardWriter bitBoard,
						  PositionStateWriter positionState,
						  MoveCacheBoardWriter moveCache,
						  KingSquareWriter kingSquare, ZobristHashWriter hash,
						  ChessPositionReader chessPositionReader) {
		undoMove(squareBoard);

		undoMove(bitBoard);

		undoMove(positionState);

		undoMove(moveCache);

		undoMove(kingSquare);

		undoMove(hash);
	}

	@Override
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
